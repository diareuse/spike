package spike.factory

import kotlinx.atomicfu.atomic

public abstract class SingletonOwner(shared: SingletonHolder?) {

    private val holder = atomic(shared)

    public val _singletons: SingletonHolder
        get() {
            holder.value?.let { return it }
            val created = SingletonHolder()
            return if (holder.compareAndSet(null, created)) created else holder.value!!
        }

}
