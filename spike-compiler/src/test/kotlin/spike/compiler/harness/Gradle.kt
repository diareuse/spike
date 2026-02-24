package spike.compiler.harness

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import java.io.File

class GradleTestProject(
    val context: Context
) {

    data class Context(
        val runner: GradleRunner,
        val classPath: String,
        val projectDirectory: File,
        val fixtures: Sequence<File>
    ) {
        val outputFiles
            get() = projectDirectory
                .resolve("build/generated/ksp/main/kotlin")
                .walk()
                .filter { it.isFile }
    }

    fun build(
        argument: String,
        vararg arguments: String
    ): BuildResult = context.runner
        .withArguments(*arrayOf("-PtestClasspath=${context.classPath}", argument, *arguments, "--stacktrace"))
        .build()

    fun buildAndFail(
        argument: String,
        vararg arguments: String
    ): BuildResult = context.runner
        .withArguments(*arrayOf("-PtestClasspath=${context.classPath}", argument, *arguments, "--stacktrace"))
        .buildAndFail()

    class Builder(
        private val projectRoot: File
    ) {

        private var classPath: String? = null
        private var fixtures: File? = null

        fun useClassPath(body: ClassPathFilter.(Sequence<File>) -> Sequence<File> = { it }) = apply {
            classPath = System.getProperty("java.class.path")
                .splitToSequence(File.pathSeparator)
                .map(::File)
                .let { body(ClassPathFilter, it) }
                .joinToString(";")
        }

        fun cloneProjectDir(from: File) = apply {
            from.copyRecursively(projectRoot, true)
        }

        fun useFixturesDir(from: File) = apply {
            this.fixtures = from
        }

        fun build(): GradleTestProject {
            val runner = GradleRunner.create()
                .withProjectDir(projectRoot)
                .forwardOutput()
            if (classPath == null) useClassPath { it }
            val context = Context(
                runner = runner,
                classPath = checkNotNull(classPath),
                projectDirectory = projectRoot,
                fixtures = checkNotNull(fixtures).walk().filter { it.isFile }
            )
            return GradleTestProject(context)
        }

    }

}

object ClassPathFilter {

    val Kotlin = listOf("spike-core", "spike-graph", "spike-compiler")
    val Androidx = listOf("spike-androidx")
    val Ktor = listOf("spike-ktor")

    fun Sequence<File>.whitelistModules(module: String, vararg modules: String) = filterNot {
        it.name.contains("spike")
    } + filter { file ->
        file.name.contains(module) || modules.any { file.name.contains(it) }
    }

    fun Sequence<File>.whitelistModules(modules: List<String>) = filterNot {
        it.name.contains("spike")
    } + filter { file ->
        modules.any { file.name.contains(it) }
    }

}

object BuildResultTasks {
    val BuildResult.kspKotlin
        get() = requireNotNull(task(":kspKotlin")) {
            "Couldn't find kspKotlin task in build result, but found ${tasks.joinToString { it.path }}"
        }
    val BuildResult.compileKotlin
        get() = requireNotNull(task(":compileKotlin")) {
            "Couldn't find compileKotlin task in build result, but found ${tasks.joinToString { it.path }}"
        }
    val BuildResult.test
        get() = requireNotNull(task(":test")) {
            "Couldn't find test task in build result, but found ${tasks.joinToString { it.path }}"
        }
}
