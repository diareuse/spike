package spike.compiler.harness

import spike.compiler.assertion.assertContentEquals
import java.io.File
import java.lang.reflect.InvocationTargetException
import java.net.URLClassLoader
import java.util.Properties
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

abstract class HarnessCompiler {

    protected fun assertMatchesGolden(name: String, libraries: Libraries = Libraries.Kotlin) {
        val modules = modules(name, libraries)
        for (module in modules) {
            assertProcesses(module)
            val golden = testResources.resolve("$name/fixtures/${module.golden}")
            assertContentEquals(golden, module.source.generatedSources)
            assertEquals(emptyList(), module.source.generatedOutsideKotlin(), "Unexpected generated files")
            assertCompiles(module.source)
        }
        runMain(modules, libraries)
        assertFirstRoundMatches(modules, libraries)
    }

    protected fun assertRuns(name: String, libraries: Libraries = Libraries.Kotlin) {
        val modules = modules(name, libraries)
        for (module in modules) {
            assertProcesses(module)
            assertCompiles(module.source)
        }
        runMain(modules, libraries)
        assertFirstRoundMatches(modules, libraries)
    }

    protected fun assertRunsWarning(name: String, vararg warnings: String) {
        val modules = modules(name, Libraries.Kotlin)
        val reported = mutableListOf<String>()
        for (module in modules) {
            val processing = module.source.process()
            assertTrue(processing.succeeded && processing.exceptions.isEmpty(), processing.report)
            reported += processing.warnings.map { it.toString() }
            assertCompiles(module.source)
        }
        for (warning in warnings) assertContains(reported.joinToString("\n"), warning)
        runMain(modules, Libraries.Kotlin)
    }

    protected fun assertMethodsFitJit(name: String, libraries: Libraries = Libraries.Kotlin) {
        val modules = modules(name, libraries)
        for (module in modules) {
            assertProcesses(module)
            assertCompiles(module.source)
        }
        runMain(modules, libraries)
        val oversized = modules.flatMap { module ->
            module.source.classes.walk().filter { it.extension == "class" }.flatMap { file ->
                BytecodeMethodSizes(file).read()
                    .filterValues { it > HotSpotHugeMethodLimit }
                    .map { (method, size) -> "${file.nameWithoutExtension}.$method: $size bytes" }
            }
        }
        assertEquals(emptyList(), oversized, "Methods the JIT refuses to compile")
    }

    protected fun assertProcessingFails(
        name: String,
        vararg messages: String,
        libraries: Libraries = Libraries.Kotlin
    ) {
        val modules = modules(name, libraries)
        for (module in modules.dropLast(1)) {
            assertProcesses(module)
            assertCompiles(module.source)
        }
        val processing = modules.last().source.process()
        assertEquals(emptyList(), processing.exceptions, "Processing $name crashed instead of reporting an error")
        assertFalse(processing.succeeded, "Processing $name was expected to fail")
        assertTrue(processing.errors.isNotEmpty(), "Processing $name failed without reporting an error")
        for (message in messages) {
            assertContains(processing.report, message)
        }
        val errors = testResources.resolve("$name/errors.txt")
        if (errors.isFile) assertEquals(errors.readText().trimEnd(), processing.report.trimEnd())
        if (runsInFirstRound(modules, libraries)) {
            val output = IsolatedKsp(modules.last().source.copyIn(FirstRoundDirectory), withConverters = false)
                .use { it.runFailing() }
            assertContains(output, FirstRoundMessage)
            for (message in messages) assertContains(output, message)
            if (errors.isFile) errors.readLines().forEach { assertContains(output, it) }
        }
    }

    private fun assertFirstRoundMatches(modules: List<FixtureModule>, libraries: Libraries) {
        if (!runsInFirstRound(modules, libraries)) return
        for (module in modules) {
            val firstRound = module.source.copyIn(FirstRoundDirectory)
            assertContains(IsolatedKsp(firstRound, withConverters = false).use { it.run() }, FirstRoundMessage)
            assertEquals(contentsOf(module.source.generatedSources), contentsOf(firstRound.generatedSources))
        }
    }

    private fun runsInFirstRound(modules: List<FixtureModule>, libraries: Libraries) =
        libraries == Libraries.Kotlin && modules.none { it.source.needsTestProcessor }

    private fun contentsOf(directory: File) = directory.walk().filter { it.isFile }
        .associate { it.relativeTo(directory).path to it.readText() }

    protected fun assertCompilationFails(
        name: String,
        vararg messages: String,
        libraries: Libraries = Libraries.Kotlin
    ) {
        val module = modules(name, libraries).single()
        assertProcesses(module)
        val compilation = module.source.compile()
        assertFalse(compilation.succeeded, "Compiling $name was expected to fail")
        for (message in messages) {
            assertContains(compilation.messages, message)
        }
    }

    private fun assertProcesses(module: FixtureModule) {
        val processing = module.source.process()
        assertTrue(processing.succeeded && processing.exceptions.isEmpty(), processing.report)
    }

    private fun assertCompiles(source: SourceModule) {
        val compilation = source.compile()
        assertTrue(compilation.succeeded, compilation.messages)
    }

    private fun runMain(modules: List<FixtureModule>, libraries: Libraries) {
        val classes = modules.map { it.source.classes }
        val urls = (classes + libraries.files()).map { it.toURI().toURL() }.toTypedArray()
        URLClassLoader(urls, javaClass.classLoader).use { loader ->
            val main = loader.loadClass(mainClassIn(classes.last())).getMethod("main")
            try {
                main.invoke(null)
            } catch (e: InvocationTargetException) {
                throw e.targetException
            }
        }
    }

    private fun mainClassIn(classes: File): String = classes.walk()
        .single { it.name == "MainKt.class" }
        .relativeTo(classes).path
        .removeSuffix(".class")
        .replace(File.separatorChar, '.')

    private fun SourceModule.generatedOutsideKotlin(): List<File> =
        generated.walk().filter { it.isFile && !it.startsWith(generatedSources) }.toList()

    private fun modules(name: String, libraries: Libraries): List<FixtureModule> {
        val project = testResources.resolve("$name/project")
        val work = File("build/tmp/compilations").resolve(name).apply { deleteRecursively() }
        val declared = project.resolve("modules.txt")
        if (declared.isFile) return declaredModules(project, work, declared, libraries)
        if (!project.resolve("app").isDirectory) {
            val main = SourceModule("main", project.resolve("src"), libraries.files(), work, options(project))
            return listOf(FixtureModule("main", main, "main/kotlin"))
        }
        val modules = mutableListOf<FixtureModule>()
        for (name in ModuleOrder) {
            if (!project.resolve(name).isDirectory) continue
            val visible = modules.filter { it.name != ImplementationModule || name == ImplementationConsumer }
            val source = SourceModule(
                name = name,
                sources = project.resolve("$name/src"),
                libraries = libraries.files() + visible.map { it.source.classes },
                workDirectory = work.resolve(name),
                options = options(project.resolve(name))
            )
            modules += FixtureModule(name, source, "$name/main/kotlin")
        }
        return modules
    }

    private fun declaredModules(project: File, work: File, declared: File, libraries: Libraries): List<FixtureModule> {
        val classpaths = mutableMapOf<String, List<File>>()
        return declared.readLines().filter(String::isNotBlank).map { line ->
            val name = line.substringBefore(':').trim()
            val dependencies = line.substringAfter(':', "").split(' ').filter(String::isNotBlank)
            val module = SourceModule(
                name = name,
                sources = project.resolve("$name/src"),
                libraries = libraries.files() + dependencies.flatMap(classpaths::getValue).distinct(),
                workDirectory = work.resolve(name),
                options = options(project.resolve(name))
            )
            classpaths[name] = dependencies.flatMap(classpaths::getValue) + module.classes
            FixtureModule(name, module, "$name/main/kotlin")
        }
    }

    private fun options(module: File): Map<String, String> {
        val file = module.resolve("ksp.properties")
        if (!file.isFile) return emptyMap()
        val properties = Properties().apply { file.reader().use(::load) }
        return properties.stringPropertyNames().associateWith(properties::getProperty)
    }

    private class FixtureModule(val name: String, val source: SourceModule, val golden: String)

    private companion object {
        const val HotSpotHugeMethodLimit = 8000
        const val FirstRoundDirectory = "first_round"
        const val FirstRoundMessage = "Spike generated in the first KSP round"
        const val ImplementationModule = "implementation"
        const val ImplementationConsumer = "library"
        val ModuleOrder = listOf(ImplementationModule, "library", "feature", "app", "test")
    }

}
