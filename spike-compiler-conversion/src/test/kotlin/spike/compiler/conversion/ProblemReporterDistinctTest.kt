package spike.compiler.conversion

import kotlin.test.Test
import kotlin.test.assertEquals

class ProblemReporterDistinctTest {

    @Test
    fun `reports a problem found twice on the same declaration once`() {
        val delegate = FakeProblemReporter()
        val reporter = ProblemReporterDistinct(delegate)
        val repeated = problem("SPIKE-A")

        reporter.report(repeated)
        reporter.report(repeated.copy())

        assertEquals(listOf(repeated), delegate.reported)
    }

    @Test
    fun `reports different problems on the same declaration`() {
        val delegate = FakeProblemReporter()
        val reporter = ProblemReporterDistinct(delegate)
        val first = problem("SPIKE-A")
        val second = problem("SPIKE-B", first.node)

        reporter.report(first)
        reporter.report(second)

        assertEquals(listOf(first, second), delegate.reported)
    }
}
