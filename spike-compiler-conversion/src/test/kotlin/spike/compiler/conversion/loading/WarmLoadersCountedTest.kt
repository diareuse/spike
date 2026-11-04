package spike.compiler.conversion.loading

import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Files
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.test.assertTrue

class WarmLoadersCountedTest {

    private val slot = FakeSlot()
    private val loaders = WarmLoadersCounted(slot)
    private val parent = ClassLoader.getPlatformClassLoader()
    private val jar = jar()

    @Test
    fun `an unchanged classpath is leased the same loader`() {
        val first = loaders.lease(classpath())
        val second = loaders.lease(classpath())
        assertFalse(first.reused)
        assertTrue(second.reused)
        assertSame(first.loader, second.loader)
    }

    @Test
    fun `a jar whose content changed gets a new loader`() {
        val first = loaders.lease(classpath())
        changeJar()
        val second = loaders.lease(classpath())
        assertFalse(second.reused)
        assertNotSame(first.loader, second.loader)
    }

    @Test
    fun `a new parent gets a new loader so KSP's API types always match KSP`() {
        val first = loaders.lease(classpath())
        val second = loaders.lease(ProcessorClasspath(listOf(jar.toURI().toURL()), URLClassLoader(emptyArray(), parent)))
        assertFalse(second.reused)
        assertNotSame(first.loader, second.loader)
    }

    @Test
    fun `a replaced loader stays open until its last lease is released`() {
        val first = loaders.lease(classpath())
        changeJar()
        loaders.lease(classpath())
        assertTrue(canLoad(first))
        first.release()
        assertFalse(canLoad(first))
    }

    @Test
    fun `the current loader stays open after every lease is released`() {
        val lease = loaders.lease(classpath())
        lease.release()
        assertTrue(canLoad(lease))
    }

    @Test
    fun `an evicted loader is not leased again and closes with its last lease`() {
        val failed = loaders.lease(classpath())
        val other = loaders.lease(classpath())
        failed.evict()
        val next = loaders.lease(classpath())
        assertFalse(next.reused)
        assertNotSame(failed.loader, next.loader)
        assertTrue(canLoad(other))
        other.release()
        assertFalse(canLoad(other))
    }

    @Test
    fun `evicting twice does not close a loader another lease still holds`() {
        val first = loaders.lease(classpath())
        val second = loaders.lease(classpath())
        val third = loaders.lease(classpath())
        first.evict()
        second.evict()
        assertTrue(canLoad(third))
    }

    @Test
    fun `releasing a lease twice counts once`() {
        val first = loaders.lease(classpath())
        val second = loaders.lease(classpath())
        changeJar()
        loaders.lease(classpath())
        first.release()
        first.release()
        assertTrue(canLoad(second))
    }

    @Test
    fun `a directory on the classpath cannot be leased because its size hides changed classes`() {
        val directory = Files.createTempDirectory("processor").toUri().toURL()
        assertFailsWith<IllegalStateException> { loaders.lease(ProcessorClasspath(listOf(directory), parent)) }
    }

    @Test
    fun `leasing, releasing and replacing race safely through the platform slot`() {
        val platform = WarmLoadersCounted(WarmLoaderSlotPlatform())
        val jars = List(3) { jar() }
        val pool = Executors.newFixedThreadPool(8)
        val attempts = List(200) { index ->
            Callable {
                val lease = platform.lease(ProcessorClasspath(listOf(jars[index % 3].toURI().toURL()), parent))
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
        val leases = pool.invokeAll(List(64) { Callable { loaders.lease(classpath()) } }).map { it.get() }
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

    private class FakeSlot : WarmLoaderSlot {
        private var entry: WarmLoaderEntry? = null

        @Synchronized
        override fun update(replace: (WarmLoaderEntry?) -> WarmLoaderEntry?) = replace(entry).also { entry = it }
    }

    private companion object {
        const val Resource = "probe.txt"
    }
}
