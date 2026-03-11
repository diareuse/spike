package spike.compiler

import spike.compiler.harness.TestFixtureGenerator
import kotlin.test.Ignore
import kotlin.test.Test

@Ignore("This test is only to generate a benchmark")
class FixtureGenerator {

    @Test
    fun generateFixtures() {
        TestFixtureGenerator().generate(10)
    }

}
