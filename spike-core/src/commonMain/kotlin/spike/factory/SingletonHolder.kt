package spike.factory

import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import kotlinx.atomicfu.update

public class SingletonHolder {

    private val backend: AtomicRef<Map<Int, Any>> = atomic(emptyMap())
    private val locks: AtomicRef<Map<Int, SynchronizedObject>> = atomic(emptyMap())
    private val scopes: AtomicRef<Map<String, SingletonHolder>> = atomic(emptyMap())

    public fun scope(key: String): SingletonHolder = scopes.getOrInsert(key) { SingletonHolder() }

    public fun <T : Any> getOrPut(id: Int, factory: () -> T): T {
        val value = backend.value[id] ?: synchronized(locks.getOrInsert(id) { SynchronizedObject() }) {
            backend.value[id] ?: factory().also { instance -> backend.update { it + (id to instance) } }
        }
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
                val consumerBuilds = singleton < 0 || backend.value[singleton] == null
                if (consumerRuns && consumerBuilds) needed[step] = true
            }
        }
        return needed
    }

    private inline fun <K, V> AtomicRef<Map<K, V>>.getOrInsert(key: K, create: () -> V): V {
        while (true) {
            val current = value
            current[key]?.let { return it }
            val created = create()
            if (compareAndSet(current, current + (key to created))) return created
        }
    }

}
