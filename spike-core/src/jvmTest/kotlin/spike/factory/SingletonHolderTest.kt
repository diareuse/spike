package spike.factory

import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.test.assertTrue

class SingletonHolderTest {

    @Test
    fun `creates the instance once when many threads request it at the same time`() {
        val holder = SingletonHolder()
        val threads = 8
        val created = AtomicInteger()
        val everyoneInFactory = CountDownLatch(threads)
        val start = CountDownLatch(1)
        val results = ConcurrentLinkedQueue<Any>()
        val pool = Executors.newFixedThreadPool(threads)

        repeat(threads) {
            pool.execute {
                start.await()
                results += holder.getOrPut(0) {
                    created.incrementAndGet()
                    everyoneInFactory.countDown()
                    everyoneInFactory.await(200, TimeUnit.MILLISECONDS)
                    Any()
                }
            }
        }
        start.countDown()
        pool.shutdown()
        assertTrue(pool.awaitTermination(10, TimeUnit.SECONDS))

        assertEquals(1, created.get())
        assertEquals(1, results.distinct().size)
    }

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
