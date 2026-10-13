package spike.compiler

import spike.compiler.harness.Template.MultiModule
import spike.compiler.harness.Template.Standalone
import spike.compiler.harness.GradleHarness
import kotlin.test.Test

class GradleBuildTest : GradleHarness() {

    @Test
    fun `ktor plugin handles requests`() = assertBuilds("ktor", Standalone, tasks = listOf(":test"))

    @Test
    fun `expect actual gets resolved`() =
        assertMatchesGolden("expect_actual", Standalone, tasks = listOf("assemble", ":jvmRun"))

    @Test
    fun `exports are taken into account`() =
        assertMatchesGolden("multi_module", MultiModule, listOf(":app:run"), modules = listOf("app", "library"))

    @Test
    fun `viewmodels share singletons of an exported library`() =
        assertBuilds("androidx_viewmodel_export", MultiModule, tasks = listOf(":app:run"))

}
