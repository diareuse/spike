package spike.compiler.harness

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import java.io.File

class FixtureProject(private val name: String, template: String?) {

    val directory: File = File("build/tmp/projects").resolve(name)
    val golden: File = testResources.resolve("$name/fixtures")

    init {
        directory.deleteRecursively()
        testResources.resolve("templates/gradle.properties").copyTo(directory.resolve("gradle.properties"))
        template?.let { testResources.resolve("templates/$it").copyRecursively(directory) }
        testResources.resolve("$name/project").copyRecursively(directory, overwrite = true)
        writeSettings()
    }

    fun build(tasks: List<String>): BuildResult = runner(tasks).build()

    fun generatedSources(module: String): File = directory.resolve(module).resolve("build/generated/ksp")

    private fun runner(tasks: List<String>) = GradleRunner.create()
        .withProjectDir(directory)
        .forwardOutput()
        .withArguments(tasks + "--stacktrace")

    private fun writeSettings() {
        val spikeRoot = File("..").canonicalPath
        val settings = directory.resolve("settings.gradle.kts")
        val declared = if (settings.exists()) settings.readText() else ""
        settings.writeText(
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
                        from(files("$spikeRoot/gradle/libs.versions.toml"))
                    }
                }
            }

            includeBuild("$spikeRoot") {
                dependencySubstitution {
                    substitute(module("local.spike:spike-compiler")).using(project(":spike-compiler"))
                    substitute(module("local.spike:spike")).using(project(":spike-core"))
                    substitute(module("local.spike:spike-ktor")).using(project(":spike-ktor"))
                    substitute(module("local.spike:spike-androidx")).using(project(":spike-androidx"))
                    substitute(module("local.spike:spike-androidx-compose")).using(project(":spike-androidx-compose"))
                }
            }

            rootProject.name = "project"

            """.trimIndent() + declared
        )
    }

}
