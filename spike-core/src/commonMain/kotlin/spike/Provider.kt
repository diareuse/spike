package spike

import kotlin.reflect.KProperty

public fun interface Provider<T> {
    public fun get(): T

    public companion object {
        public operator fun <T> Provider<T>.invoke(): T = get()
        public operator fun <T> Provider<T>.getValue(any: Any?, property: KProperty<*>): T = get()
    }
}