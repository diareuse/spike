package spike

public fun interface Provider<T> {
    public fun get(): T

    public companion object {
        public operator fun <T> Provider<T>.invoke(): T = get()
    }
}