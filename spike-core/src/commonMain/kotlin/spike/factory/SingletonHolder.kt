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

    private fun <K, V> AtomicRef<Map<K, V>>.getOrInsert(key: K, create: () -> V): V {
        while (true) {
            val current = value
            current[key]?.let { return it }
            val created = create()
            if (compareAndSet(current, current + (key to created))) return created
        }
    }

}
