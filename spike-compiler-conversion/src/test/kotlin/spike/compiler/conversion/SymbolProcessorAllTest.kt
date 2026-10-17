package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSAnnotated
import kotlin.test.Test
import kotlin.test.assertEquals

class SymbolProcessorAllTest {

    @Test
    fun `runs every processor in each round`() {
        val first = FakeSymbolProcessor()
        val second = FakeSymbolProcessor()

        SymbolProcessorAll(listOf(first, second)).process(unused())

        assertEquals(listOf(1, 1), listOf(first.rounds, second.rounds))
    }

    @Test
    fun `defers the symbols every processor defers`() {
        val a = unused<KSAnnotated>()
        val b = unused<KSAnnotated>()
        val processor = SymbolProcessorAll(listOf(FakeSymbolProcessor(listOf(a)), FakeSymbolProcessor(listOf(b))))

        assertEquals(listOf(a, b), processor.process(unused()))
    }
}
