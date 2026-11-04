package spike.compiler.conversion.loading

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import spike.compiler.conversion.unused
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProcessorSourceWarmTest {

    private val logger = RecordingLogger()
    private val providerName = FailingProvider::class.java.name
    private val providerBytes = FailingProvider::class.java.classLoader
        .getResourceAsStream(providerName.replace('.', '/') + ".class")!!.readBytes()

    @Test
    fun `a processor that fails to start evicts its loader and keeps the failure`() {
        var evicted = false
        val loader = ProviderLoader(jar())
        val lease = WarmLease(loader, false, onEvict = { evicted = true }, onAbandon = {}, onRelease = {})
        val source = ProcessorSourceWarm(implementationIn(loader)) { _, _ -> lease }
        val failure = assertFailsWith<IllegalStateException> { source.processor(environment()) }
        assertEquals("create failed", failure.message)
        assertTrue(evicted)
    }

    @Test
    fun `a provider outside a URLClassLoader cannot be warmed and leases nothing`() {
        var leased = false
        val source = ProcessorSourceWarm(implementationIn(PlainLoader())) { _, _ ->
            leased = true
            error("must not lease")
        }
        val failure = assertFailsWith<IllegalStateException> { source.processor(environment()) }
        assertContains(failure.message.orEmpty(), "outside a URLClassLoader")
        assertFalse(leased)
    }

    @Suppress("UNCHECKED_CAST")
    private fun implementationIn(loader: ClassLoader) =
        ProcessorImplementation(loader.loadClass(providerName) as Class<out SymbolProcessorProvider>)

    private fun jar(): URL = Files.createTempFile("processor", ".jar").toFile().apply {
        ZipOutputStream(outputStream()).use { it.putNextEntry(ZipEntry("empty.txt")) }
        deleteOnExit()
    }.toURI().toURL()

    private fun environment() = SymbolProcessorEnvironment(
        options = emptyMap(),
        kotlinVersion = KotlinVersion.CURRENT,
        codeGenerator = unused(),
        logger = logger,
    )

    private inner class ProviderLoader(jar: URL) : URLClassLoader(arrayOf(jar), ProcessorSourceWarmTest::class.java.classLoader) {
        override fun loadClass(name: String, resolve: Boolean): Class<*> =
            if (name == providerName) findLoadedClass(name) ?: defineClass(name, providerBytes, 0, providerBytes.size)
            else super.loadClass(name, resolve)
    }

    private inner class PlainLoader : ClassLoader(ProcessorSourceWarmTest::class.java.classLoader) {
        override fun loadClass(name: String, resolve: Boolean): Class<*> =
            if (name == providerName) findLoadedClass(name) ?: defineClass(name, providerBytes, 0, providerBytes.size)
            else super.loadClass(name, resolve)
    }
}

class FailingProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = error("create failed")
}
