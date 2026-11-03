package spike.compiler

import spike.compiler.harness.HarnessGradle
import kotlin.test.Test

class GradleBuildTest : HarnessGradle() {

    @Test
    fun `ktor plugin handles requests`() = assertBuilds("ktor", tasks = listOf(":test"))

    @Test
    fun `expect actual gets resolved`() = assertMatchesGolden("expect_actual", tasks = listOf("assemble", ":jvmRun"))

    @Test
    fun `an expect Include property resolves to its actual`() =
        assertBuilds("expect_actual_property", tasks = listOf("assemble", ":jvmRun"))

    @Test
    fun `an incremental build after editing an unrelated file keeps the entry point factory`() =
        assertRebuildsAfterEdit("incremental_edit", "src/main/Screen.kt", "\"first\"", "\"second\"")

    @Test
    fun `the Gradle plugin sets up a JVM project in one line`() =
        assertBuilds("gradle_plugin_jvm", tasks = listOf(":run"))

    @Test
    fun `the Gradle plugin sets up every target of a multiplatform project`() =
        assertBuilds("gradle_plugin_multiplatform", tasks = listOf(":compileKotlinJs", ":jvmRun"))

    @Test
    fun `the Gradle plugin adds Spike in the version it was published as`() = assertBuilds(
        "gradle_plugin_version",
        tasks = listOf(":assertSpikeVersion"),
        arguments = listOf("-PVERSION_NAME=1.2.3")
    )

    @Test
    fun `the Gradle plugin lets a library module's ViewModels reach the app's entry point without an export`() =
        assertBuilds("gradle_plugin_viewmodel_library", tasks = listOf(":app:run"))

    @Test
    fun `a test source set reuses the app's ViewModel entry point and can replace its bindings`() =
        assertBuilds("gradle_plugin_viewmodel_test_source_set", tasks = listOf(":runTestMain"))

    @Test
    fun `exports are taken into account`() = assertMatchesGolden(
        "multi_module",
        template = "multi_module",
        tasks = listOf(":app:run"),
        modules = listOf("app", "library")
    )

    @Test
    fun `an export depending on another module's export with imports builds and runs`() =
        assertBuilds("export_depends_on_export", tasks = listOf(":app:run"))

}
