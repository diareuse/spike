package spike.factory

import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SingletonOwnerConcurrencyTest {

    private class Owner : SingletonOwner(null)

    @Test
    fun `threads asking a fresh owner at the same time all get the same holder`() {
        val threads = 8
        repeat(200) {
            val owner = Owner()
            val start = CountDownLatch(1)
            val holders = ConcurrentLinkedQueue<SingletonHolder>()
            val pool = Executors.newFixedThreadPool(threads)
            repeat(threads) {
                pool.execute {
                    start.await()
                    holders += owner._singletons
                }
            }
            start.countDown()
            pool.shutdown()
            assertTrue(pool.awaitTermination(10, TimeUnit.SECONDS))

            assertEquals(1, holders.distinct().size)
        }
    }

}
