package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSAnnotated
import kotlin.test.Test
import kotlin.test.assertEquals

class SymbolProcessorRejectingTest {

    @Test
    fun `reports every symbol it finds`() {
        val first = unused<KSAnnotated>()
        val second = unused<KSAnnotated>()
        val reporter = FakeProblemReporter()

        val processor = SymbolProcessorRejecting({ listOf(first, second) }, "SPIKE-NO", "Not converted.", reporter)
        processor.process(unused())

        assertEquals(
            listOf(first, second).map { ConversionProblem("SPIKE-NO", "Not converted.", it) },
            reporter.reported,
        )
    }
}
