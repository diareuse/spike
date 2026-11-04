package spike.compiler.conversion.loading

import spike.Include
import java.net.URLClassLoader
import java.util.Collections
import java.util.WeakHashMap
import java.util.concurrent.atomic.AtomicInteger

@Include
class WarmLoadersCounted(private val slot: WarmLoaderSlot) : WarmLoaders {
    override fun lease(classpath: ProcessorClasspath, run: Any): WarmLease {
        val key = classpath.key
        var reused = false
        val entry = checkNotNull(slot.update { current ->
            if (current != null && current.key == key && current.loader.parent === classpath.parent) {
                reused = true
                current.apply { join(run) }
            } else {
                current?.let(::release)
                val loader = URLClassLoader(classpath.urls.toTypedArray(), classpath.parent)
                val runs = Collections.synchronizedMap(WeakHashMap<Any, AtomicInteger>())
                WarmLoaderEntry(key, loader, AtomicInteger(1), runs).apply { join(run) }
            }
        })
        return WarmLease(
            loader = entry.loader,
            reused = reused,
            onEvict = { evict(entry) },
            onAbandon = {
                evict(entry)
                if (entry.runs.remove(run) != null) release(entry)
            },
            onRelease = { entry.leave(run) },
        )
    }

    private fun WarmLoaderEntry.join(run: Any) {
        runs.getOrPut(run) {
            references.incrementAndGet()
            AtomicInteger()
        }.incrementAndGet()
    }

    private fun WarmLoaderEntry.leave(run: Any) {
        val processors = runs[run] ?: return
        if (processors.decrementAndGet() > 0) return
        runs.remove(run)
        release(this)
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
