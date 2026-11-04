package spike.compiler.conversion.loading

import spike.Include
import java.lang.ref.WeakReference
import java.net.URLClassLoader
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * The state that outlives a run. Its classes are loaded again for each run, so it holds JDK types only.
 */
class WarmLoaderEntry(
    val key: String,
    val loader: URLClassLoader,
    val runs: MutableMap<WeakReference<Any>, AtomicInteger>,
    val closable: AtomicBoolean,
) {
    fun toStored(): List<Any> = listOf(key, loader, runs, closable)

    companion object {
        fun ofStored(stored: Any?): WarmLoaderEntry? {
            val parts = stored as? List<*> ?: return null
            @Suppress("UNCHECKED_CAST")
            return WarmLoaderEntry(
                key = parts[0] as String,
                loader = parts[1] as URLClassLoader,
                runs = parts[2] as MutableMap<WeakReference<Any>, AtomicInteger>,
                closable = parts[3] as AtomicBoolean,
            )
        }
    }
}

fun interface WarmLoaderSlot {
    fun update(replace: (WarmLoaderEntry?) -> WarmLoaderEntry?): WarmLoaderEntry?

    companion object {
        @Include
        val WarmLoaderSlotPlatform.slot: WarmLoaderSlot
            get() = this
    }
}
