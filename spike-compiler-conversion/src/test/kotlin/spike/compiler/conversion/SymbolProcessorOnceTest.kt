package spike.compiler.conversion

import kotlin.test.Test
import kotlin.test.assertEquals

class SymbolProcessorOnceTest {

    @Test
    fun `converts symbols in the first round only`() {
        val delegate = FakeSymbolProcessor()
        val processor = SymbolProcessorOnce(delegate)

        repeat(3) { processor.process(unused()) }

        assertEquals(1, delegate.rounds)
    }

    @Test
    fun `defers nothing in later rounds`() {
        val processor = SymbolProcessorOnce(FakeSymbolProcessor(deferred = listOf(unused())))
        processor.process(unused())

        assertEquals(emptyList(), processor.process(unused()))
    }
}
