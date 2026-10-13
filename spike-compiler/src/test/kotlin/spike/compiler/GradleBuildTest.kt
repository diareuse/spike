package spike.compiler

import spike.compiler.harness.HarnessGradle
import kotlin.test.Test

class GradleBuildTest : HarnessGradle() {

    @Test
    fun `ktor plugin handles requests`() = assertBuilds("ktor", tasks = listOf(":test"))

    @Test
    fun `expect actual gets resolved`() = assertMatchesGolden("expect_actual", tasks = listOf("assemble", ":jvmRun"))

    @Test
    fun `exports are taken into account`() = assertMatchesGolden(
        "multi_module",
        template = "multi_module",
        tasks = listOf(":app:run"),
        modules = listOf("app", "library")
    )

}
