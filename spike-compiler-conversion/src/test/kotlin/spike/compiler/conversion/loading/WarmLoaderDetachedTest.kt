package spike.compiler.conversion.loading

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import spike.compiler.conversion.FakeSymbolProcessor
import spike.compiler.conversion.unused
import java.net.URLClassLoader
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class WarmLoaderDetachedTest {

    private val logger = RecordingLogger()
    private val cold = FakeSymbolProcessor()
    private val parent = ClassLoader.getPlatformClassLoader()
    private val classpath = ProcessorClasspath(listOf(jar().toURI().toURL()), parent)

    @Test
    fun `a detached loader reads the classpath it was given under the parent it was given`() {
        val loader = WarmLoaderDetached(ContextFreeCallSubject()).loader(classpath)
        assertEquals(classpath.urls, loader.urLs.toList())
        assertSame(parent, loader.parent)
    }

    @Test
    fun `a call that keeps the context of its caller is refused`() {
        val keeping = WarmLoaderDetached { it.invokeWithArguments() }
        val failure = assertFailsWith<IllegalStateException> { keeping.loader(classpath) }
        assertContains(failure.message.orEmpty(), "context")
    }

    @Test
    fun `contexts carry their callers exactly on the JDKs that still track them`() {
        assertEquals(Runtime.version().feature() < FirstJdkWithoutContexts, CallerContextsProbed().carried())
    }

    @Test
    fun `a JDK whose contexts cannot carry a caller builds a plain loader without the detached road`() {
        val refusing = WarmLoaderDetached { error("the detached road must not run") }
        val loader = warmLoaderOf(refusing) { false }(classpath)
        assertEquals(classpath.urls, loader.urLs.toList())
        assertSame(parent, loader.parent)
    }

    @Test
    fun `a JDK whose contexts carry their callers takes the detached road`() {
        val refusing = WarmLoaderDetached { error("the detached road ran") }
        val failure = assertFailsWith<IllegalStateException> { warmLoaderOf(refusing) { true }(classpath) }
        assertContains(failure.message.orEmpty(), "the detached road ran")
    }

    @Test
    fun `a JDK that cannot detach the loader builds cold instead of pinning it`() {
        val keeping = WarmLoaderDetached { it.invokeWithArguments() }
        assertSame(cold, ProcessorSourceFallback(warmThrough(keeping)) { cold }.processor(environment()))
        assertContains(logger.infos.single(), "could not keep")
    }

    @Test
    fun `a JDK without the mechanism builds cold`() {
        val missing = WarmLoaderDetached { throw NoSuchMethodError("doAsPrivileged") }
        assertSame(cold, ProcessorSourceFallback(warmThrough(missing)) { cold }.processor(environment()))
    }

    @Suppress("UNCHECKED_CAST")
    private fun warmThrough(detached: WarmLoaderDetached): ProcessorSource {
        val type = PerRunLoader().loadClass(ProviderName) as Class<out SymbolProcessorProvider>
        val loaders = WarmLoadersCounted(WarmLoaderSlotPlatform(), logger, detached::loader)
        return ProcessorSourceWarm(ProcessorImplementation(type), loaders)
    }

    private fun jar() = Files.createTempFile("processor", ".jar").toFile().apply {
        ZipOutputStream(outputStream()).use { it.putNextEntry(ZipEntry("empty.txt")) }
        deleteOnExit()
    }

    private fun environment() = SymbolProcessorEnvironment(
        options = emptyMap(),
        kotlinVersion = KotlinVersion.CURRENT,
        codeGenerator = unused(),
        logger = logger,
    )

    private inner class PerRunLoader :
        URLClassLoader(arrayOf(jar().toURI().toURL()), WarmLoaderDetachedTest::class.java.classLoader) {
        override fun loadClass(name: String, resolve: Boolean): Class<*> {
            if (name != ProviderName) return super.loadClass(name, resolve)
            val bytes = parent.getResourceAsStream(name.replace('.', '/') + ".class")!!.use { it.readBytes() }
            return findLoadedClass(name) ?: defineClass(name, bytes, 0, bytes.size)
        }
    }

    private companion object {
        const val FirstJdkWithoutContexts = 24
        val ProviderName: String = StartingProvider::class.java.name
    }
}

class StartingProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = FakeSymbolProcessor()
}
