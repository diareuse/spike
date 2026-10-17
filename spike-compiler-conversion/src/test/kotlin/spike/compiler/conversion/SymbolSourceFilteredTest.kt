package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSAnnotated
import kotlin.test.Test
import kotlin.test.assertEquals

class SymbolSourceFilteredTest {

    @Test
    fun `finds only the symbols it keeps`() {
        val kept = unused<KSAnnotated>()
        val skipped = unused<KSAnnotated>()
        val source = SymbolSourceFiltered({ listOf(kept, skipped) }, keep = { it === kept })

        assertEquals(listOf(kept), source.symbols(unused()))
    }
}
