package spike.compiler.generator

import spike.compiler.graph.Type
import spike.compiler.graph.TypeFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LinearLayoutByCostTest {

    private val layouts = LinearLayoutByCost(ChunksByCost(budget = 2)) { 1 }

    @Test
    fun `a program within budget is one part without slots`() {
        val layout = layouts.of(program(emptyList(), listOf(0)))

        assertEquals(listOf(listOf(0, 1)), layout.parts.map { part -> part.map { it.index } })
        assertEquals(emptyMap(), layout.slots)
    }

    @Test
    fun `the root is the last step of the last part`() {
        val layout = layouts.of(program(emptyList(), listOf(0), listOf(1), listOf(2)))

        assertEquals(3, layout.root)
        assertEquals(1, layout.partOf(layout.root))
    }

    @Test
    fun `a value consumed only in its own part stays local without a slot`() {
        val layout = layouts.of(program(emptyList(), listOf(0), emptyList(), listOf(1, 2)))

        assertTrue(layout.isLocal(0))
        assertNull(layout.slotOf(0))
    }

    @Test
    fun `a value consumed in a later part gets a slot and is not local`() {
        val layout = layouts.of(program(emptyList(), emptyList(), listOf(0), listOf(1, 2)))

        assertFalse(layout.isLocal(0))
        assertEquals(0, layout.slotOf(0))
    }

    @Test
    fun `a value consumed in its own part and a later part is local and has a slot`() {
        val layout = layouts.of(program(emptyList(), listOf(0), emptyList(), listOf(0, 1, 2)))

        assertTrue(layout.isLocal(0))
        assertEquals(0, layout.slotOf(0))
    }

    @Test
    fun `slots are numbered densely in step order`() {
        val layout = layouts.of(
            program(emptyList(), emptyList(), emptyList(), emptyList(), listOf(0, 2), listOf(1, 3, 4))
        )

        assertEquals(mapOf(0 to 0, 1 to 1, 2 to 2, 3 to 3), layout.slots)
    }

    private fun program(vararg arguments: List<Int>) = LinearProgram(
        arguments.mapIndexed { index, indices -> LinearStep(factory(index), indices) }
    )

    private fun factory(index: Int) = TypeFactory.Property(Type.Simple("test", "Step$index", false), "step$index")

}
