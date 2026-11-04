package spike.compiler.harness

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import java.io.File
import java.util.Properties
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

class FixtureProject(private val name: String, template: String?) {

    val directory: File = File("build/tmp/projects").resolve(name)
    val golden: File = testResources.resolve("$name/fixtures")

    init {
        directory.deleteRecursively()
        testResources.resolve("templates/gradle.properties").copyTo(directory.resolve("gradle.properties"))
        template?.let { testResources.resolve("templates/$it").copyRecursively(directory) }
        testResources.resolve("$name/project").copyRecursively(directory, overwrite = true)
        writeSettings()
        writeAndroidSdk()
    }

    fun build(tasks: List<String>, arguments: List<String> = emptyList()): BuildResult =
        runner(tasks + arguments).build()

    fun buildAndFail(tasks: List<String>): BuildResult = runner(tasks).buildAndFail()

    fun replace(path: String, oldText: String, newText: String) {
        val file = directory.resolve(path)
        val text = file.readText()
        check(oldText in text) { "$path does not contain $oldText" }
        file.writeText(text.replace(oldText, newText))
    }

    fun delete(path: String) {
        check(directory.resolve(path).delete()) { "$path could not be deleted" }
    }

    fun copyCompilerJar() {
        File(System.getProperty("spike.compiler.jar")).copyTo(directory.resolve(CompilerJar), overwrite = true)
    }

    fun changeCompilerJar() {
        val jar = directory.resolve(CompilerJar)
        val changed = directory.resolve("$CompilerJar.changed")
        ZipFile(jar).use { source ->
            ZipOutputStream(changed.outputStream()).use { target ->
                for (entry in source.entries()) {
                    target.putNextEntry(ZipEntry(entry.name))
                    val bytes = source.getInputStream(entry).use { it.readBytes() }
                    target.write(if (entry.name == MarkerClass) withChangedMarker(bytes) else bytes)
                    target.closeEntry()
                }
            }
        }
        changed.copyTo(jar, overwrite = true)
        changed.delete()
    }

    private fun withChangedMarker(classFile: ByteArray) = classFile.toString(Charsets.ISO_8859_1)
        .replace("SpikeRound", "SpikeSwaps")
        .toByteArray(Charsets.ISO_8859_1)

    fun generatedSources(module: String): File = directory.resolve(module).resolve("build/generated/ksp")

    private fun runner(tasks: List<String>) = GradleRunner.create()
        .withProjectDir(directory)
        .forwardOutput()
        .withArguments(tasks + "--stacktrace")

    private fun writeAndroidSdk() {
        val usesAndroid = directory.walk().any { it.name == "build.gradle.kts" && AndroidPlugin in it.readText() }
        if (!usesAndroid) return
        val sdk = System.getenv("ANDROID_HOME")?.takeIf(String::isNotBlank) ?: rootSdk()
        checkNotNull(sdk) {
            "$name builds an Android project, but neither ANDROID_HOME nor sdk.dir in the Spike root local.properties " +
                "points at an Android SDK."
        }
        directory.resolve("local.properties").writeText("sdk.dir=$sdk\n")
    }

    private fun rootSdk(): String? {
        val file = File("..").resolve("local.properties")
        if (!file.isFile) return null
        return Properties().apply { file.reader().use(::load) }.getProperty("sdk.dir")
    }

    private fun writeSettings() {
        val spikeRoot = File("..").canonicalPath
        val usesPlugin = directory.walk().any { it.name == "build.gradle.kts" && SpikePlugin in it.readText() }
        val pluginBuild = if (usesPlugin) "includeBuild(\"$spikeRoot\")" else ""
        val settings = directory.resolve("settings.gradle.kts")
        val declared = if (settings.exists()) settings.readText() else ""
        settings.writeText(
            """
            pluginManagement {
                $pluginBuild
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

    private companion object {
        const val CompilerJar = "compiler.jar"
        const val MarkerClass = "spike/compiler/processor/symbol/SettleMarker.class"
        const val SpikePlugin = "id(\"io.github.diareuse.spike\")"
        const val AndroidPlugin = "libs.plugins.android."
    }
}
