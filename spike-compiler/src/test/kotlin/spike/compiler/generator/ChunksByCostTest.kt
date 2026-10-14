package spike.compiler.generator

import kotlin.test.Test
import kotlin.test.assertEquals

class ChunksByCostTest {

    private val chunks = ChunksByCost(budget = 10)

    @Test
    fun `no items make no chunks`() {
        assertEquals(emptyList(), chunks.split(emptyList<Int>()) { it })
    }

    @Test
    fun `items that fit the budget together share one chunk`() {
        assertEquals(listOf(listOf(4, 6)), chunks.split(listOf(4, 6)) { it })
    }

    @Test
    fun `an item that would exceed the budget starts a new chunk`() {
        assertEquals(listOf(listOf(4, 5), listOf(2, 3)), chunks.split(listOf(4, 5, 2, 3)) { it })
    }

    @Test
    fun `an item costlier than the budget gets a chunk of its own`() {
        assertEquals(listOf(listOf(1), listOf(30), listOf(1)), chunks.split(listOf(1, 30, 1)) { it })
    }

    @Test
    fun `chunks keep the order of the items`() {
        assertEquals((1..20).toList(), chunks.split((1..20).toList()) { 3 }.flatten())
    }

}
