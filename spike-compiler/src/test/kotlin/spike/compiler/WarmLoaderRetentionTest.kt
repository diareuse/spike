package spike.compiler

import spike.compiler.harness.IsolatedKsp
import spike.compiler.harness.Libraries
import spike.compiler.harness.SourceModule
import java.io.File
import java.lang.ref.WeakReference
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

class WarmLoaderRetentionTest {

    @Test
    fun `nothing KSP hands out for a run outlives it while the compiler classes stay loaded`() {
        val work = File("build/tmp/compilations/warm_loader_retention").apply { deleteRecursively() }
        val sources = File("src/test/resources/warm_loader/project/src/main")
        IsolatedKsp(SourceModule("main", sources, Libraries.Kotlin.files(), work)).use { ksp ->
            ksp.run()
            val output = ksp.run()
            assertContains(output, "Spike reused compiler classes that were already loaded")
            assertFalse("Spike loaded the compiler classes" in output, output)
            assertFalse("could not keep" in output, output)
            assertEquals(emptyList(), survivorsOf(ksp))
        }
    }

    @Test
    fun `nothing KSP hands out outlives a run that generated in the first round`() {
        val work = File("build/tmp/compilations/warm_loader_retention_first_round").apply { deleteRecursively() }
        val sources = File("src/test/resources/warm_loader/project/src/main")
        IsolatedKsp(SourceModule("main", sources, Libraries.Kotlin.files(), work), withConverters = false).use { ksp ->
            ksp.run()
            val output = ksp.run()
            assertContains(output, "Spike generated in the first KSP round")
            assertFalse("Spike loaded the compiler classes" in output, output)
            assertEquals(emptyList(), survivorsOf(ksp))
        }
    }

    @Test
    fun `a loader replaced by a new classpath is unloaded`() {
        val work = File("build/tmp/compilations/warm_loader_unloading").apply { deleteRecursively() }
        val sources = File("src/test/resources/warm_loader/project/src/main")
        IsolatedKsp(SourceModule("main", sources, Libraries.Kotlin.files(), work)).use { ksp ->
            ksp.run()
            val replaced = WeakReference(ksp.warmLoader())
            ksp.addToProcessorClasspath(emptyJar(work))
            ksp.run()
            assertTrue(unloaded(replaced), "The replaced loader is still reachable")
        }
    }

    private fun emptyJar(work: File) = work.resolve("empty.jar").apply {
        parentFile.mkdirs()
        ZipOutputStream(outputStream()).use { it.putNextEntry(ZipEntry("empty.txt")) }
    }

    private fun unloaded(reference: WeakReference<*>): Boolean {
        val start = TimeSource.Monotonic.markNow()
        while (start.elapsedNow() < 10.seconds) {
            System.gc()
            if (reference.get() == null) return true
        }
        return false
    }

    private fun survivorsOf(ksp: IsolatedKsp): List<Any> {
        val start = TimeSource.Monotonic.markNow()
        while (start.elapsedNow() < 10.seconds) {
            System.gc()
            if (ksp.handed.all { it.get() == null }) return emptyList()
        }
        return ksp.handed.mapNotNull { it.get() }
    }
}
