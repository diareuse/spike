package spike.compiler.conversion.loading

import spike.Include
import java.net.URLClassLoader
import java.util.concurrent.atomic.AtomicInteger

/**
 * The state that outlives a run. Its classes are loaded again for each run, so it holds JDK types only.
 */
class WarmLoaderEntry(val key: String, val loader: URLClassLoader, val references: AtomicInteger) {
    fun toStored(): List<Any> = listOf(key, loader, references)

    companion object {
        fun ofStored(stored: Any?): WarmLoaderEntry? {
            val parts = stored as? List<*> ?: return null
            return WarmLoaderEntry(parts[0] as String, parts[1] as URLClassLoader, parts[2] as AtomicInteger)
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
