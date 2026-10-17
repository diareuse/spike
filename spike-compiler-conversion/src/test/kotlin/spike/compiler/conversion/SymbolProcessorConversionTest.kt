package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSAnnotated
import kotlin.test.Test
import kotlin.test.assertEquals

class SymbolProcessorConversionTest {

    private val accepted = unused<KSAnnotated>()
    private val rejected = unused<KSAnnotated>()
    private val rejection = problem("SPIKE-REJECTED", rejected)
    private val conversion = FakeConversion()
    private val reporter = FakeProblemReporter()
    private val processor = SymbolProcessorConversion(
        source = { listOf(accepted, rejected) },
        check = { if (it === rejected) rejection else null },
        conversion = conversion,
        reporter = reporter,
    )

    @Test
    fun `converts every symbol that passes the check`() {
        processor.process(unused())

        assertEquals(listOf(accepted), conversion.converted)
    }

    @Test
    fun `reports every symbol that fails the check instead of converting it`() {
        processor.process(unused())

        assertEquals(listOf(rejection), reporter.reported)
    }

    @Test
    fun `defers no symbols`() = assertEquals(emptyList(), processor.process(unused()))
}
