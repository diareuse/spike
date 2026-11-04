package spike.factory

import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
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
    fun `singletons created at the same time by different threads are all kept`() {
        val threads = 8
        val idsPerThread = 64
        repeat(50) {
            val holder = SingletonHolder()
            val start = CountDownLatch(1)
            val first = ConcurrentHashMap<Int, Any>()
            val pool = Executors.newFixedThreadPool(threads)
            repeat(threads) { thread ->
                pool.execute {
                    start.await()
                    repeat(idsPerThread) { step ->
                        val id = step * threads + thread
                        first[id] = holder.getOrPut(id) { Any() }
                    }
                }
            }
            start.countDown()
            pool.shutdown()
            assertTrue(pool.awaitTermination(10, TimeUnit.SECONDS))

            assertEquals(threads * idsPerThread, first.size)
            for ((id, instance) in first) {
                assertSame(instance, holder.getOrPut<Any>(id) { error("singleton $id was lost") })
            }
        }
    }

    @Test
    fun `concurrent requests build each id once across many fresh holders even when the first attempt fails`() {
        val threads = 8
        val failingFirst = listOf(0, 65536)
        val ids = failingFirst + listOf(1, 2, 65535)
        val pool = Executors.newFixedThreadPool(threads)
        repeat(2000) {
            val holder = SingletonHolder()
            val barrier = CyclicBarrier(threads)
            val attempts = ids.associateWith { AtomicInteger() }
            val builds = ids.associateWith { AtomicInteger() }
            val instances = ids.associateWith { ConcurrentLinkedQueue<Any>() }

            val workers = List(threads) {
                pool.submit {
                    barrier.await(10, TimeUnit.SECONDS)
                    for (id in ids) {
                        runCatching {
                            holder.getOrPut(id) {
                                val attempt = attempts.getValue(id).getAndIncrement()
                                check(attempt > 0 || id !in failingFirst) { "first attempt" }
                                builds.getValue(id).incrementAndGet()
                                Any()
                            }
                        }.onSuccess { instances.getValue(id) += it }
                    }
                }
            }
            workers.forEach { it.get(10, TimeUnit.SECONDS) }

            for (id in ids) {
                assertEquals(1, builds.getValue(id).get(), "successful builds of id $id")
                assertEquals(1, instances.getValue(id).distinct().size, "instances of id $id")
            }
        }
        pool.shutdown()
    }

    @Test
    fun `a thread blocked on a failing build builds the singleton itself`() {
        val holder = SingletonHolder()
        val building = CountDownLatch(1)
        val fail = CountDownLatch(1)
        val failure = CompletableFuture<Throwable>()
        val built = CompletableFuture<Any>()

        thread {
            runCatching {
                holder.getOrPut<Any>(0) {
                    building.countDown()
                    fail.await(10, TimeUnit.SECONDS)
                    error("first attempt")
                }
            }.onFailure(failure::complete)
        }
        assertTrue(building.await(10, TimeUnit.SECONDS))
        val waiter = thread { built.complete(holder.getOrPut(0) { "built by the waiter" }) }
        awaitBlocked(waiter)
        fail.countDown()

        assertEquals("first attempt", failure.get(10, TimeUnit.SECONDS).message)
        assertEquals("built by the waiter", built.get(10, TimeUnit.SECONDS))
        assertEquals("built by the waiter", holder.getOrPut(0) { "built again" })
    }

    private fun awaitBlocked(waiter: Thread) {
        val deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10)
        while (waiter.state != Thread.State.BLOCKED) {
            assertTrue(System.nanoTime() < deadline, "waiter never blocked, state ${waiter.state}")
            Thread.onSpinWait()
        }
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
