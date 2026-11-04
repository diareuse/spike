package spike.factory

import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import kotlinx.atomicfu.update

private const val DENSE_IDS = 1 shl 16
private val NoSingletons = arrayOfNulls<Any>(0)

public class SingletonHolder {

    private val dense: AtomicRef<Array<Any?>> = atomic(NoSingletons)
    private val sparse: AtomicRef<Map<Int, Any>?> = atomic(null)
    private val locks = HashMap<Int, SynchronizedObject>()
    private val locksGuard = SynchronizedObject()
    private val scopes: AtomicRef<Map<String, SingletonHolder>> = atomic(emptyMap())

    public fun scope(key: String): SingletonHolder = scopes.getOrInsert(key) { SingletonHolder() }

    public fun <T : Any> getOrPut(id: Int, factory: () -> T): T {
        val value = stored(id) ?: create(id, factory)
        @Suppress("UNCHECKED_CAST")
        return value as T
    }

    public fun needed(table: String): BooleanArray {
        val needed = BooleanArray(table[0].code - 1)
        var cursor = 1
        for (step in needed.indices) {
            val consumers = table[cursor++].code - 1
            repeat(consumers) {
                val consumer = table[cursor++].code - 1
                val singleton = table[cursor++].code - 1
                val consumerRuns = consumer < 0 || needed[consumer]
                val consumerBuilds = singleton < 0 || stored(singleton) == null
                if (consumerRuns && consumerBuilds) needed[step] = true
            }
        }
        return needed
    }

    private fun stored(id: Int): Any? {
        val values = dense.value
        return if (id in values.indices) values[id] else sparse.value?.get(id)
    }

    private fun create(id: Int, factory: () -> Any): Any =
        synchronized(lockOf(id)) {
            stored(id) ?: factory().also { instance ->
                store(id, instance)
                release(id)
            }
        }

    private fun lockOf(id: Int): SynchronizedObject =
        synchronized(locksGuard) { locks.getOrPut(id) { SynchronizedObject() } }

    private fun release(id: Int) {
        synchronized(locksGuard) { locks.remove(id) }
    }

    private fun store(id: Int, instance: Any) {
        if (id in 0 until DENSE_IDS) dense.update { it.with(id, instance) }
        else sparse.update { it.orEmpty() + (id to instance) }
    }

    private fun Array<Any?>.with(id: Int, instance: Any): Array<Any?> =
        copyOf(maxOf(size, id + 1)).also { it[id] = instance }

    private inline fun <K, V> AtomicRef<Map<K, V>>.getOrInsert(key: K, create: () -> V): V {
        while (true) {
            val current = value
            current[key]?.let { return it }
            val created = create()
            if (compareAndSet(current, current + (key to created))) return created
        }
    }

}
