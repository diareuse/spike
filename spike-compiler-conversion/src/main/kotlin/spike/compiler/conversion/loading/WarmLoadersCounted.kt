package spike.compiler.conversion.loading

import com.google.devtools.ksp.processing.KSPLogger
import spike.Include
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.URLClassLoader
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * KSP says nothing when a processor crashes, so runs are held weakly: a crashed run neither blocks a close nor stays
 * reachable, and a loader it was the last to use is left to the collector, which closes its jars.
 */
@Include
class WarmLoadersCounted(
    private val slot: WarmLoaderSlot,
    private val logger: KSPLogger,
    private val loaderOf: (ProcessorClasspath) -> URLClassLoader,
) : WarmLoaders {
    override fun lease(classpath: ProcessorClasspath, run: Any): WarmLease {
        val key = classpath.key
        var reused = false
        var replaced: WarmLoaderEntry? = null
        val entry = checkNotNull(slot.update { current ->
            if (current != null && current.key == key && current.loader.parent === classpath.parent) {
                reused = true
                current.apply { join(run) }
            } else {
                val runs = ConcurrentHashMap<WeakReference<Any>, AtomicInteger>()
                val replacement = WarmLoaderEntry(key, loaderOf(classpath), runs, AtomicBoolean()).apply { join(run) }
                replaced = current
                replacement
            }
        })
        replaced?.let(::retire)
        return WarmLease(
            loader = entry.loader,
            reused = reused,
            onEvict = { evict(entry) },
            onAbandon = {
                entry.referenceTo(run)?.let(entry.runs::remove)
                evict(entry)
                closeIfDone(entry)
            },
            onRelease = {
                entry.leave(run)
                closeIfDone(entry)
            },
        )
    }

    private fun WarmLoaderEntry.join(run: Any) {
        runs.keys.removeIf { it.get() == null }
        val reference = referenceTo(run) ?: WeakReference(run).also { runs[it] = AtomicInteger() }
        runs.getValue(reference).incrementAndGet()
    }

    private fun WarmLoaderEntry.leave(run: Any) {
        val reference = referenceTo(run) ?: return
        if (runs[reference]?.decrementAndGet() == 0) runs.remove(reference)
    }

    private fun WarmLoaderEntry.referenceTo(run: Any) = runs.keys.firstOrNull { it.get() === run }

    private fun evict(entry: WarmLoaderEntry) {
        var evicted = false
        slot.update { current ->
            if (current?.loader !== entry.loader) return@update current
            evicted = true
            null
        }
        if (evicted) retire(entry)
    }

    private fun retire(entry: WarmLoaderEntry) {
        entry.closable.set(true)
        closeIfDone(entry)
    }

    private fun closeIfDone(entry: WarmLoaderEntry) {
        val done = entry.runs.keys.all { it.get() == null }
        if (done && entry.closable.compareAndSet(true, false)) close(entry.loader)
    }

    private fun close(loader: URLClassLoader) = try {
        loader.close()
    } catch (e: IOException) {
        logger.info("Spike could not close a class loader it no longer uses: $e")
    }
}
