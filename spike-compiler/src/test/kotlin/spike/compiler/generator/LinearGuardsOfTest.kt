package spike.compiler.generator

import spike.compiler.graph.Type
import spike.compiler.graph.TypeFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LinearGuardsOfTest {

    @Test
    fun `a program without singletons has no guards`() {
        val guards = guardsOf(singletons = emptyMap()).of(program(emptyList(), listOf(0)))

        assertEquals("", guards.table)
        assertNull(guards.guardOf(0))
    }

    @Test
    fun `the arguments of a singleton root are guarded by that singleton`() {
        val guards = guardsOf(singletons = mapOf(1 to 9)).of(program(emptyList(), listOf(0)))

        assertEquals(0, guards.guardOf(0))
        assertEquals(table(1, 1, -1, 9), guards.table)
    }

    @Test
    fun `a step also consumed by a step that always runs is not guarded`() {
        val guards = guardsOf(singletons = mapOf(1 to 9)).of(program(emptyList(), listOf(0), listOf(0, 1)))

        assertNull(guards.guardOf(0))
        assertNull(guards.guardOf(1))
    }

    @Test
    fun `a step consumed only by a guarded step is guarded through it`() {
        val guards = guardsOf(singletons = mapOf(2 to 9)).of(program(emptyList(), listOf(0), listOf(1)))

        assertEquals(0, guards.guardOf(1))
        assertEquals(1, guards.guardOf(0))
        assertEquals(table(2, 1, -1, 9, 1, 0, -1), guards.table)
    }

    @Test
    fun `a nested singleton runs but its arguments are guarded by it`() {
        val guards = guardsOf(singletons = mapOf(1 to 9)).of(program(emptyList(), listOf(0), listOf(1)))

        assertNull(guards.guardOf(1))
        assertEquals(0, guards.guardOf(0))
        assertEquals(table(1, 1, -1, 9), guards.table)
    }

    @Test
    fun `a program whose table exceeds the limit has no guards`() {
        val guards = guardsOf(singletons = mapOf(1 to 9), limit = 3).of(program(emptyList(), listOf(0)))

        assertEquals("", guards.table)
        assertNull(guards.guardOf(0))
    }

    @Test
    fun `a program whose table needs a value outside the plain character range has no guards`() {
        val guards = guardsOf(singletons = mapOf(1 to 0xD7FF)).of(program(emptyList(), listOf(0)))

        assertEquals("", guards.table)
        assertNull(guards.guardOf(0))
    }

    private fun guardsOf(singletons: Map<Int, Int>, limit: Int = Int.MAX_VALUE) =
        LinearGuardsOf(limit) { step -> singletons[step.factory.name()] }

    private fun TypeFactory.name() = (this as TypeFactory.Property).name.removePrefix("step").toInt()

    private fun program(vararg arguments: List<Int>) = LinearProgram(
        arguments.mapIndexed { index, indices -> LinearStep(factory(index), indices) }
    )

    private fun factory(index: Int) = TypeFactory.Property(Type.Simple("test", "Step$index", false), "step$index")

    private fun table(vararg values: Int) = values.joinToString("") { (it + 1).toChar().toString() }

}
