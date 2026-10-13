package spike.factory

import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SingletonHolderConcurrencyTest {

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
    fun `a singleton factory may wait for another thread creating a different singleton`() {
        val holder = SingletonHolder()

        val outer = holder.getOrPut(0) {
            CompletableFuture.supplyAsync { holder.getOrPut(1) { "inner" } }.get(2, TimeUnit.SECONDS)
        }

        assertEquals("inner", outer)
    }
}
