package spike.compiler

import spike.compiler.assertion.assertContentEquals
import spike.compiler.harness.BuildResultTasks.compileKotlin
import spike.compiler.harness.BuildResultTasks.kspKotlin
import spike.compiler.harness.BuildResultTasks.test
import spike.compiler.harness.TestHarness
import kotlin.test.Test

class ExecTest : TestHarness() {

    @Test
    fun `objects are composed`() = runTest(
        label = "compositor",
        prepare = { useClassPath { it.whitelistModules(Kotlin) }.build() },
        test = { build("kspKotlin", "compileKotlin", "run") },
        verify = {
            assertSuccess(it.compileKotlin)
            assertSuccess(it.kspKotlin)
            fixtures.assertContentEquals(outputFiles)
        }
    )

}