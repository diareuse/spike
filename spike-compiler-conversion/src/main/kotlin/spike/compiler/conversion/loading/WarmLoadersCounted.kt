package spike.compiler.conversion.loading

import spike.Include
import java.net.URLClassLoader
import java.util.concurrent.atomic.AtomicInteger

@Include
class WarmLoadersCounted(private val slot: WarmLoaderSlot) : WarmLoaders {
    override fun lease(classpath: ProcessorClasspath): WarmLease {
        val key = classpath.key
        var reused = false
        val entry = checkNotNull(slot.update { current ->
            if (current != null && current.key == key && current.loader.parent === classpath.parent) {
                current.references.incrementAndGet()
                reused = true
                current
            } else {
                current?.let(::release)
                val loader = URLClassLoader(classpath.urls.toTypedArray(), classpath.parent)
                WarmLoaderEntry(key, loader, AtomicInteger(2))
            }
        })
        return WarmLease(entry.loader, reused, onEvict = { evict(entry) }, onRelease = { release(entry) })
    }

    private fun evict(entry: WarmLoaderEntry) {
        slot.update { current ->
            if (current?.loader !== entry.loader) return@update current
            release(current)
            null
        }
    }

    private fun release(entry: WarmLoaderEntry) {
        if (entry.references.decrementAndGet() == 0) entry.loader.close()
    }
}
