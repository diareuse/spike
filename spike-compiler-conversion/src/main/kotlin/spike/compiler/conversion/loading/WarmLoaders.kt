package spike.compiler.conversion.loading

import spike.Include
import java.io.File
import java.net.URL
import java.util.concurrent.atomic.AtomicBoolean

class ProcessorClasspath(val urls: List<URL>, val parent: ClassLoader) {
    val key: String
        get() = urls.joinToString("\n") { url ->
            val file = File(url.toURI())
            check(file.isFile) { "$file is not a jar, so its size and time cannot show that its classes changed" }
            "${file.path}:${file.length()}:${file.lastModified()}"
        }
}

class WarmLease(
    val loader: ClassLoader,
    val reused: Boolean,
    private val onEvict: () -> Unit,
    private val onAbandon: () -> Unit,
    private val onRelease: () -> Unit,
) {
    private val released = AtomicBoolean()

    fun release() {
        if (released.compareAndSet(false, true)) onRelease()
    }

    fun evict() {
        onEvict()
        release()
    }

    fun abandon() {
        if (released.compareAndSet(false, true)) onAbandon()
    }
}

fun interface WarmLoaders {
    fun lease(classpath: ProcessorClasspath, run: Any): WarmLease

    companion object {
        @Include
        val WarmLoadersCounted.loaders: WarmLoaders
            get() = this
    }
}
