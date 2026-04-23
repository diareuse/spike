package spike.factory

import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic

public class SingletonHolder {

    private val backend: AtomicRef<Map<Int, Any>> = atomic(emptyMap())

    public fun <T : Any> getOrPut(id: Int, factory: () -> T): T {
        val value = backend.value[id] ?: busyPut(id, factory())
        @Suppress("UNCHECKED_CAST")
        return value as T
    }

    private fun <T : Any> busyPut(id: Int, instance: T): T {
        val pair = id to instance
        while (true) {
            val ref = backend.value
            if (ref.containsKey(id)) {
                @Suppress("UNCHECKED_CAST")
                return ref[id] as T
            }
            if (backend.compareAndSet(ref, ref + pair)) {
                return instance
            }
        }
    }

}