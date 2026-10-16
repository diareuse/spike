package spike.factory

import kotlin.test.Test
import kotlin.test.assertContentEquals

class SingletonHolderNeededTest {

    @Test
    fun `a step consumed only by a cached singleton is not needed`() {
        val holder = SingletonHolder()
        holder.getOrPut(7) { "cached" }

        val needed = holder.needed(table(1, 1, -1, 7))

        assertContentEquals(booleanArrayOf(false), needed)
    }

    @Test
    fun `a step consumed by a singleton that is not cached yet is needed`() {
        val needed = SingletonHolder().needed(table(1, 1, -1, 7))

        assertContentEquals(booleanArrayOf(true), needed)
    }

    @Test
    fun `a step consumed only by a step that is not needed is not needed`() {
        val holder = SingletonHolder()
        holder.getOrPut(7) { "cached" }

        val needed = holder.needed(table(2, 1, -1, 7, 1, 0, -1))

        assertContentEquals(booleanArrayOf(false, false), needed)
    }

    @Test
    fun `a step is needed when any of its consumers needs it`() {
        val holder = SingletonHolder()
        holder.getOrPut(7) { "cached" }

        val needed = holder.needed(table(1, 2, -1, 7, -1, 8))

        assertContentEquals(booleanArrayOf(true), needed)
    }

    private fun table(vararg values: Int) = values.joinToString("") { (it + 1).toChar().toString() }
}
