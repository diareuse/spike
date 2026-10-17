package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSAnnotated
import kotlin.test.Test
import kotlin.test.assertEquals

class SymbolProcessorUncheckedTest {

    @Test
    fun `converts every symbol it finds`() {
        val symbols = listOf(unused<KSAnnotated>(), unused<KSAnnotated>())
        val conversion = FakeConversion()

        SymbolProcessorUnchecked({ symbols }, conversion).process(unused())

        assertEquals(symbols, conversion.converted)
    }
}
