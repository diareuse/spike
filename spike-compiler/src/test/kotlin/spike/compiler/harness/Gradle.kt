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
            "Couldn't find kspKotlin task in build result, but found ${tasks.joinToString { it.path }}"
        }
}

object Fixtures {

    val androidx_viewmodel get() = getFixturesDir("androidx_viewmodel")
    val bind get() = getFixturesDir("bind")
    val circular get() = getFixturesDir("circular")
    val complex get() = getFixturesDir("complex")
    val compositor get() = getFixturesDir("compositor")
    val entry_factory get() = getFixturesDir("entry_factory")
    val entrypoint get() = getFixturesDir("entrypoint")
    val klass get() = getFixturesDir("klass")
    val marked_constructors get() = getFixturesDir("marked_constructors")
    val multibinding get() = getFixturesDir("multibinding")
    val no_companion get() = getFixturesDir("no_companion")
    val no_marked_constructors get() = getFixturesDir("no_marked_constructors")
    val out_variance get() = getFixturesDir("out_variance")
    val provider get() = getFixturesDir("provider")
    val qualifier get() = getFixturesDir("qualifier")

    private fun getFixturesDir(testCase: String): File {
        val fixturesUrl = Fixtures::class.java.classLoader.getResource("$testCase/fixtures")
        check(fixturesUrl?.protocol == "file") { "Fixtures for $testCase not found" }
        return File(fixturesUrl.toURI().path)
    }

}

object Projects {

    val androidx_viewmodel get() = getProjectDir("androidx_viewmodel")
    val bind get() = getProjectDir("bind")
    val circular get() = getProjectDir("circular")
    val complex get() = getProjectDir("complex")
    val compositor get() = getProjectDir("compositor")
    val entry_factory get() = getProjectDir("entry_factory")
    val entrypoint get() = getProjectDir("entrypoint")
    val klass get() = getProjectDir("klass")
    val marked_constructors get() = getProjectDir("marked_constructors")
    val multibinding get() = getProjectDir("multibinding")
    val no_companion get() = getProjectDir("no_companion")
    val no_marked_constructors get() = getProjectDir("no_marked_constructors")
    val out_variance get() = getProjectDir("out_variance")
    val provider get() = getProjectDir("provider")
    val qualifier get() = getProjectDir("qualifier")

    private fun getProjectDir(testCase: String): File {
        val fixturesUrl = Fixtures::class.java.classLoader.getResource("$testCase/project")
        check(fixturesUrl?.protocol == "file") { "Project dir for $testCase not found" }
        return File(fixturesUrl.toURI().path)
    }

}