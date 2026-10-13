package spike.compiler.harness

import spike.compiler.assertion.assertContentEquals
import java.io.File
import java.lang.reflect.InvocationTargetException
import java.net.URLClassLoader
import kotlin.test.assertContains
import kotlin.test.assertFalse
import kotlin.test.assertTrue

abstract class CompilerHarness {

    protected fun assertMatchesGolden(name: String, libraries: Libraries = Libraries.Kotlin) {
        val module = modules(name, libraries).single()
        assertProcesses(module)
        assertContentEquals(resources.resolve("$name/fixtures/main/kotlin"), module.generatedSources)
        assertCompiles(module)
        runMain(listOf(module))
    }

    protected fun assertRuns(name: String, libraries: Libraries = Libraries.Kotlin) {
        val modules = modules(name, libraries)
        for (module in modules) {
            assertProcesses(module)
            assertCompiles(module)
        }
        runMain(modules)
    }

    protected fun assertCompiles(name: String, libraries: Libraries = Libraries.Kotlin) {
        val module = modules(name, libraries).single()
        assertProcesses(module)
        assertCompiles(module)
    }

    protected fun assertProcessingFails(
        name: String,
        vararg messages: String,
        libraries: Libraries = Libraries.Kotlin
    ) {
        val outcome = modules(name, libraries).single().process()
        assertFalse(outcome.succeeded, "Processing $name was expected to fail")
        assertTrue(outcome.messages.isNotBlank(), "Processing $name failed without reporting an error")
        for (message in messages) {
            assertContains(outcome.messages, message)
        }
    }

    protected fun assertCompilationFails(
        name: String,
        vararg messages: String,
        libraries: Libraries = Libraries.Kotlin
    ) {
        val module = modules(name, libraries).single()
        assertProcesses(module)
        val outcome = module.compile()
        assertFalse(outcome.succeeded, "Compiling $name was expected to fail")
        for (message in messages) {
            assertContains(outcome.messages, message)
        }
    }

    private fun assertProcesses(module: SourceModule) {
        val outcome = module.process()
        assertTrue(outcome.succeeded, outcome.messages)
    }

    private fun assertCompiles(module: SourceModule) {
        val outcome = module.compile()
        assertTrue(outcome.succeeded, outcome.messages)
    }

    private fun runMain(modules: List<SourceModule>) {
        val classes = modules.map { it.classes }
        val loader = URLClassLoader(classes.map { it.toURI().toURL() }.toTypedArray(), javaClass.classLoader)
        val main = loader.loadClass(mainClassIn(classes.last())).getMethod("main")
        try {
            main.invoke(null)
        } catch (e: InvocationTargetException) {
            throw e.targetException
        }
    }

    private fun mainClassIn(classes: File): String = classes.walk()
        .single { it.name == "MainKt.class" }
        .relativeTo(classes).path
        .removeSuffix(".class")
        .replace(File.separatorChar, '.')

    private fun modules(name: String, libraries: Libraries): List<SourceModule> {
        val project = resources.resolve("$name/project")
        val work = File("build/tmp/compilations").resolve(name).apply { deleteRecursively() }
        val names = listOf("library", "app").filter { project.resolve(it).isDirectory }.ifEmpty { listOf("") }
        val compiled = mutableListOf<File>()
        return names.map { module ->
            SourceModule(
                sources = project.resolve(module).resolve("src"),
                libraries = libraries.files() + compiled,
                workDirectory = work.resolve(module)
            ).also { compiled += it.classes }
        }
    }

    private companion object {
        val resources = File("src/test/resources")
    }

}
