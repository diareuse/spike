package spike.compiler.harness

import spike.compiler.assertion.assertContentEquals
import java.io.File
import java.lang.reflect.InvocationTargetException
import java.net.URLClassLoader
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
        runMain(modules)
    }

    protected fun assertRuns(name: String, libraries: Libraries = Libraries.Kotlin) {
        val modules = modules(name, libraries)
        for (module in modules) {
            assertProcesses(module)
            assertCompiles(module.source)
        }
        runMain(modules)
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
        runMain(modules)
    }

    protected fun assertMethodsFitJit(name: String, libraries: Libraries = Libraries.Kotlin) {
        val modules = modules(name, libraries)
        for (module in modules) {
            assertProcesses(module)
            assertCompiles(module.source)
        }
        runMain(modules)
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
    }

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

    private fun runMain(modules: List<FixtureModule>) {
        val classes = modules.map { it.source.classes }
        URLClassLoader(classes.map { it.toURI().toURL() }.toTypedArray(), javaClass.classLoader).use { loader ->
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
        if (!project.resolve("app").isDirectory) {
            val main = SourceModule("main", project.resolve("src"), libraries.files(), work)
            return listOf(FixtureModule(main, "main/kotlin"))
        }
        val library = SourceModule(
            name = "library",
            sources = project.resolve("library/src"),
            libraries = libraries.files(),
            workDirectory = work.resolve("library")
        )
        val app = SourceModule(
            name = "app",
            sources = project.resolve("app/src"),
            libraries = libraries.files() + library.classes,
            workDirectory = work.resolve("app")
        )
        return listOf(FixtureModule(library, "library/main/kotlin"), FixtureModule(app, "app/main/kotlin"))
    }

    private class FixtureModule(val source: SourceModule, val golden: String)

    private companion object {
        const val HotSpotHugeMethodLimit = 8000
    }

}
