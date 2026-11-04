package spike.compiler.conversion.loading

import java.io.File
import java.io.IOException
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Files
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.test.assertTrue

class WarmLoadersCountedTest {

    private val slot = FakeSlot()
    private val logger = RecordingLogger()
    private val loaders = WarmLoadersCounted(slot, logger) { URLClassLoader(it.urls.toTypedArray(), it.parent) }
    private val stubborn = WarmLoadersCounted(slot, logger) { LoaderThatFailsToClose(it) }
    private val parent = ClassLoader.getPlatformClassLoader()
    private val jar = jar()

    @Test
    fun `an unchanged classpath is leased the same loader`() {
        val first = loaders.lease(classpath(), Any())
        val second = loaders.lease(classpath(), Any())
        assertFalse(first.reused)
        assertTrue(second.reused)
        assertSame(first.loader, second.loader)
    }

    @Test
    fun `a jar whose content changed gets a new loader`() {
        val first = loaders.lease(classpath(), Any())
        changeJar()
        val second = loaders.lease(classpath(), Any())
        assertFalse(second.reused)
        assertNotSame(first.loader, second.loader)
    }

    @Test
    fun `a new parent gets a new loader so KSP's API types always match KSP`() {
        val first = loaders.lease(classpath(), Any())
        val second = loaders.lease(ProcessorClasspath(listOf(jar.toURI().toURL()), URLClassLoader(emptyArray(), parent)), Any())
        assertFalse(second.reused)
        assertNotSame(first.loader, second.loader)
    }

    @Test
    fun `a replaced loader stays open until its last lease is released`() {
        val first = loaders.lease(classpath(), Any())
        changeJar()
        loaders.lease(classpath(), Any())
        assertTrue(canLoad(first))
        first.release()
        assertFalse(canLoad(first))
    }

    @Test
    fun `the current loader stays open after every lease is released`() {
        val lease = loaders.lease(classpath(), Any())
        lease.release()
        assertTrue(canLoad(lease))
    }

    @Test
    fun `an evicted loader is not leased again and closes with its last lease`() {
        val failed = loaders.lease(classpath(), Any())
        val other = loaders.lease(classpath(), Any())
        failed.evict()
        val next = loaders.lease(classpath(), Any())
        assertFalse(next.reused)
        assertNotSame(failed.loader, next.loader)
        assertTrue(canLoad(other))
        other.release()
        assertFalse(canLoad(other))
    }

    @Test
    fun `evicting twice does not close a loader another lease still holds`() {
        val first = loaders.lease(classpath(), Any())
        val second = loaders.lease(classpath(), Any())
        val third = loaders.lease(classpath(), Any())
        first.evict()
        second.evict()
        assertTrue(canLoad(third))
    }

    @Test
    fun `the processors of one run hold the loader together`() {
        val run = Any()
        val first = loaders.lease(classpath(), run)
        val sibling = loaders.lease(classpath(), run)
        changeJar()
        loaders.lease(classpath(), Any())
        first.release()
        assertTrue(canLoad(sibling))
        sibling.release()
        assertFalse(canLoad(sibling))
    }

    @Test
    fun `a run that ends in a crash lets go of the leases of all its processors`() {
        val run = Any()
        val crashed = loaders.lease(classpath(), run)
        val sibling = loaders.lease(classpath(), run)
        crashed.abandon()
        assertFalse(canLoad(sibling))
    }

    @Test
    fun `a crash in one run does not close a loader another run still uses`() {
        val other = loaders.lease(classpath(), Any())
        loaders.lease(classpath(), Any()).abandon()
        assertTrue(canLoad(other))
    }

    @Test
    fun `a sibling finishing after a crash does not count again`() {
        val run = Any()
        val other = loaders.lease(classpath(), Any())
        val crashed = loaders.lease(classpath(), run)
        val sibling = loaders.lease(classpath(), run)
        crashed.abandon()
        sibling.release()
        assertTrue(canLoad(other))
    }

    @Test
    fun `evicting for a processor that failed to start keeps the loader for its running sibling`() {
        val run = Any()
        val running = loaders.lease(classpath(), run)
        loaders.lease(classpath(), run).evict()
        assertTrue(canLoad(running))
        running.release()
        assertFalse(canLoad(running))
    }

    @Test
    fun `a loader that fails to close does not fail the release and is logged`() {
        val lease = stubborn.lease(classpath(), Any())
        changeJar()
        stubborn.lease(classpath(), Any())
        lease.release()
        assertContains(logger.infos.single(), "could not close")
        assertEquals(emptyList(), logger.warnings)
    }

    @Test
    fun `a loader that fails to close while being replaced leaves the new loader in the slot`() {
        stubborn.lease(classpath(), Any()).release()
        changeJar()
        val replacing = stubborn.lease(classpath(), Any())
        val next = stubborn.lease(classpath(), Any())
        assertTrue(next.reused)
        assertSame(replacing.loader, next.loader)
    }

    @Test
    fun `abandoning a run does not fail when its loader fails to close`() {
        stubborn.lease(classpath(), Any()).abandon()
        assertFalse(stubborn.lease(classpath(), Any()).reused)
    }

    @Test
    fun `releasing a lease twice counts once`() {
        val first = loaders.lease(classpath(), Any())
        val second = loaders.lease(classpath(), Any())
        changeJar()
        loaders.lease(classpath(), Any())
        first.release()
        first.release()
        assertTrue(canLoad(second))
    }

    @Test
    fun `a directory on the classpath cannot be leased because its size hides changed classes`() {
        val directory = Files.createTempDirectory("processor").toUri().toURL()
        assertFailsWith<IllegalStateException> { loaders.lease(ProcessorClasspath(listOf(directory), parent), Any()) }
    }

    @Test
    fun `leasing, releasing and replacing race safely through the platform slot`() {
        val platform = WarmLoadersCounted(WarmLoaderSlotPlatform(), logger) { URLClassLoader(it.urls.toTypedArray(), it.parent) }
        val jars = List(3) { jar() }
        val pool = Executors.newFixedThreadPool(8)
        val attempts = List(200) { index ->
            Callable {
                val lease = platform.lease(ProcessorClasspath(listOf(jars[index % 3].toURI().toURL()), parent), Any())
                val open = canLoad(lease)
                lease.release()
                open
            }
        }
        val open = pool.invokeAll(attempts).map { it.get() }
        pool.shutdown()
        assertEquals(emptyList(), open.filterNot { it })
    }

    @Test
    fun `concurrent leases of one classpath share one loader`() {
        val pool = Executors.newFixedThreadPool(8)
        val leases = pool.invokeAll(List(64) { Callable { loaders.lease(classpath(), Any()) } }).map { it.get() }
        pool.shutdown()
        assertEquals(1, leases.map { it.loader }.distinct().size)
        assertEquals(1, leases.count { !it.reused })
    }

    private fun classpath() = ProcessorClasspath(listOf<URL>(jar.toURI().toURL()), parent)

    private fun canLoad(lease: WarmLease) = (lease.loader as URLClassLoader).findResource(Resource) != null

    private fun File.write(vararg entries: String) = ZipOutputStream(outputStream()).use { zip ->
        for (entry in entries) {
            zip.putNextEntry(ZipEntry(entry))
            zip.closeEntry()
        }
    }

    private fun changeJar() = jar.write(Resource, "changed.txt")

    private fun jar(): File = Files.createTempFile("processor", ".jar").toFile().apply {
        write(Resource)
        deleteOnExit()
    }

    private class LoaderThatFailsToClose(classpath: ProcessorClasspath) :
        URLClassLoader(classpath.urls.toTypedArray(), classpath.parent) {
        override fun close() = throw IOException("locked")
    }

    private class FakeSlot : WarmLoaderSlot {
        private var entry: WarmLoaderEntry? = null

        @Synchronized
        override fun update(replace: (WarmLoaderEntry?) -> WarmLoaderEntry?) = replace(entry).also { entry = it }
    }

    private companion object {
        const val Resource = "probe.txt"
    }
}
