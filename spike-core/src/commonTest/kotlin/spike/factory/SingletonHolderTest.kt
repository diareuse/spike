package spike.factory

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
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
    fun `keeps every instance when a lower id is stored after a higher one`() {
        val holder = SingletonHolder()
        val high = holder.getOrPut(9) { Any() }
        val low = holder.getOrPut(2) { Any() }

        assertSame(high, holder.getOrPut<Any>(9) { error("higher id was lost") })
        assertSame(low, holder.getOrPut<Any>(2) { error("lower id was lost") })
    }

    @Test
    fun `keeps a singleton under a negative id`() {
        val holder = SingletonHolder()
        val first = holder.getOrPut(-1) { Any() }

        assertSame(first, holder.getOrPut<Any>(-1) { error("factory must not run again") })
    }

    @Test
    fun `keeps a singleton under the largest id`() {
        val holder = SingletonHolder()
        val first = holder.getOrPut(Int.MAX_VALUE) { Any() }

        assertSame(first, holder.getOrPut<Any>(Int.MAX_VALUE) { error("factory must not run again") })
    }

    @Test
    fun `keeps singletons on both sides of the dense id limit`() {
        val holder = SingletonHolder()
        val last = holder.getOrPut(65535) { Any() }
        val beyond = holder.getOrPut(65536) { Any() }

        assertNotSame(last, beyond)
        assertSame(last, holder.getOrPut<Any>(65535) { error("last dense id was lost") })
        assertSame(beyond, holder.getOrPut<Any>(65536) { error("first sparse id was lost") })
    }

    @Test
    fun `keeps the last dense id written after a lower id`() {
        val holder = SingletonHolder()
        val low = holder.getOrPut(3) { Any() }
        val last = holder.getOrPut(65535) { Any() }

        assertSame(low, holder.getOrPut<Any>(3) { error("lower id was lost") })
        assertSame(last, holder.getOrPut<Any>(65535) { error("last dense id was lost") })
    }

    @Test
    fun `a singleton whose factory failed is built again on the next request`() {
        val holder = SingletonHolder()
        assertFailsWith<IllegalStateException> { holder.getOrPut<Any>(0) { error("first attempt") } }

        assertEquals("second attempt", holder.getOrPut(0) { "second attempt" })
    }

    @Test
    fun `keeps singletons under ids far apart`() {
        val holder = SingletonHolder()
        val low = holder.getOrPut(1) { Any() }
        val high = holder.getOrPut(1_000_000_000) { Any() }

        assertSame(low, holder.getOrPut<Any>(1) { error("low id was lost") })
        assertSame(high, holder.getOrPut<Any>(1_000_000_000) { error("high id was lost") })
    }

    @Test
    fun `a singleton factory may request another singleton from the same holder`() {
        val holder = SingletonHolder()

        val outer = holder.getOrPut(0) { listOf(holder.getOrPut(1) { "inner" }) }

        assertEquals(listOf("inner"), outer)
        assertEquals("inner", holder.getOrPut(1) { "other" })
    }

    @Test
    fun `a factory requesting its own id gets one instance everywhere`() {
        val holder = SingletonHolder()
        var inner: Any? = null

        val outer = holder.getOrPut(0) {
            inner = holder.getOrPut(0) { Any() }
            Any()
        }

        assertSame(inner, outer)
        assertSame(inner, holder.getOrPut<Any>(0) { error("factory must not run again") })
    }

    @Test
    fun `a factory requesting its own id beyond the dense limit gets one instance everywhere`() {
        val holder = SingletonHolder()
        var inner: Any? = null

        val outer = holder.getOrPut(65536) {
            inner = holder.getOrPut(65536) { Any() }
            Any()
        }

        assertSame(inner, outer)
        assertSame(inner, holder.getOrPut<Any>(65536) { error("factory must not run again") })
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
