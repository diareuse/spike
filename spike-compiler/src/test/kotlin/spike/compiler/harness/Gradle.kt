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
        val fixturesDir: File
    ) {
        val outputDir
            get() = projectDirectory.resolve("build/generated/ksp/")
    }

    fun build(
        argument: String,
        vararg arguments: String
    ): BuildResult = context.runner
        .withArguments(*arrayOf(argument, *arguments, "--stacktrace"))
        .build()

    fun buildAndFail(
        argument: String,
        vararg arguments: String
    ): BuildResult = context.runner
        .withArguments(*arrayOf(argument, *arguments, "--stacktrace"))
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
            val absoluteRootDir = File("../").canonicalPath
            projectRoot.resolve("settings.gradle.kts").writeText(
                """
                pluginManagement {
                    repositories {
                        gradlePluginPortal()
                        mavenCentral()
                        google()
                    }
                }
                
                dependencyResolutionManagement {
                    repositories {
                        mavenCentral()
                        google()
                    }
                    versionCatalogs {
                        create("libs") {
                            from(files("$absoluteRootDir/gradle/libs.versions.toml"))
                        }
                    }
                }

                // This is the magic: it treats your local repo as part of the build
                includeBuild("$absoluteRootDir") {
                    dependencySubstitution {
                        substitute(module("io.github.diareuse:spike-compiler")).using(project(":spike-compiler"))
                        substitute(module("io.github.diareuse:spike")).using(project(":spike-core"))
                        substitute(module("io.github.diareuse:spike-ktor")).using(project(":spike-ktor"))
                        substitute(module("io.github.diareuse:spike-androidx")).using(project(":spike-androidx"))
                        substitute(module("io.github.diareuse:spike-androidx-compose")).using(project(":spike-androidx-compose"))
                    }
                }
                
                rootProject.name = "project"
            """.trimIndent()
            )
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
                fixturesDir = checkNotNull(fixtures)
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
    val BuildResult.assemble
        get() = requireNotNull(task(":assemble")) {
            "Couldn't find test task in build result, but found ${tasks.joinToString { it.path }}"
        }
    val BuildResult.jvmRun
        get() = requireNotNull(task(":jvmRun")) {
            "Couldn't find test task in build result, but found ${tasks.joinToString { it.path }}"
        }
}
