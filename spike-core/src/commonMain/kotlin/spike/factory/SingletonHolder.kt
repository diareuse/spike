package spike.factory

import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

public class SingletonHolder {

    private val backend: AtomicRef<Map<Int, Any>> = atomic(emptyMap())
    private val lock = SynchronizedObject()

    public fun <T : Any> getOrPut(id: Int, factory: () -> T): T {
        val value = backend.value[id] ?: synchronized(lock) {
            backend.value[id] ?: factory().also { backend.value += id to it }
        }
        @Suppress("UNCHECKED_CAST")
        return value as T
    }

}
