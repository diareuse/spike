package spike.compiler.conversion.loading

import com.google.devtools.ksp.processing.KSPLogger
import spike.Include
import java.io.IOException
import java.net.URLClassLoader
import java.util.Collections
import java.util.WeakHashMap
import java.util.concurrent.atomic.AtomicInteger

@Include
class WarmLoadersCounted(
    private val slot: WarmLoaderSlot,
    private val logger: KSPLogger,
    private val loaderOf: (ProcessorClasspath) -> URLClassLoader,
) : WarmLoaders {
    override fun lease(classpath: ProcessorClasspath, run: Any): WarmLease {
        val key = classpath.key
        var reused = false
        val entry = checkNotNull(slot.update { current ->
            if (current != null && current.key == key && current.loader.parent === classpath.parent) {
                reused = true
                current.apply { join(run) }
            } else {
                val runs = Collections.synchronizedMap(WeakHashMap<Any, AtomicInteger>())
                val replacement = WarmLoaderEntry(key, loaderOf(classpath), AtomicInteger(1), runs).apply { join(run) }
                current?.let(::release)
                replacement
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
        if (entry.references.decrementAndGet() == 0) close(entry.loader)
    }

    private fun close(loader: URLClassLoader) = try {
        loader.close()
    } catch (e: IOException) {
        logger.info("Spike could not close a class loader it no longer uses: $e")
    }
}
