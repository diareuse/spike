package spike.factory

import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

public class SingletonHolder {

    private val backend: AtomicRef<Map<Int, Any>> = atomic(emptyMap())
    private val scopes: AtomicRef<Map<String, SingletonHolder>> = atomic(emptyMap())
    private val lock = SynchronizedObject()

    public fun scope(key: String): SingletonHolder = scopes.value[key] ?: synchronized(lock) {
        scopes.value[key] ?: SingletonHolder().also { scopes.value += key to it }
    }

    public fun <T : Any> getOrPut(id: Int, factory: () -> T): T {
        val value = backend.value[id] ?: synchronized(lock) {
            backend.value[id] ?: factory().also { backend.value += id to it }
        }
        @Suppress("UNCHECKED_CAST")
        return value as T
    }

}
