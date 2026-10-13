package spike.factory

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertSame

class SingletonHolderTest {

    @Test
    fun `returns the stored instance on later requests without calling the factory`() {
        val holder = SingletonHolder()
        val first = holder.getOrPut(0) { Any() }

        val second = holder.getOrPut<Any>(0) { error("factory must not run again") }

        assertSame(first, second)
    }

    @Test
    fun `keeps separate instances for separate ids`() {
        val holder = SingletonHolder()

        assertNotSame(holder.getOrPut(0) { Any() }, holder.getOrPut(1) { Any() })
    }

    @Test
    fun `a singleton factory may request another singleton from the same holder`() {
        val holder = SingletonHolder()

        val outer = holder.getOrPut(0) { listOf(holder.getOrPut(1) { "inner" }) }

        assertEquals(listOf("inner"), outer)
        assertEquals("inner", holder.getOrPut(1) { "other" })
    }

    @Test
    fun `a scope with the same key is the same holder`() {
        val holder = SingletonHolder()

        val first = holder.scope("library").getOrPut(0) { Any() }
        val second = holder.scope("library").getOrPut(0) { Any() }

        assertSame(first, second)
    }

    @Test
    fun `scopes keep their ids apart from the parent and from each other`() {
        val holder = SingletonHolder()
        val parent = holder.getOrPut(0) { Any() }

        val library = holder.scope("library").getOrPut(0) { Any() }
        val other = holder.scope("other").getOrPut(0) { Any() }

        assertNotSame(parent, library)
        assertNotSame(library, other)
    }
}
