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
    fun `exports are taken into account`() = assertMatchesGolden(
        "multi_module",
        template = "multi_module",
        tasks = listOf(":app:run"),
        modules = listOf("app", "library")
    )

}
