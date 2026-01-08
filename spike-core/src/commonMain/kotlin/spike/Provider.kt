package spike

fun interface Provider<T> {
    fun get(): T

    companion object {
        operator fun <T> Provider<T>.invoke() = get()
    }
}