package spike.compiler

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSNode
import spike.compiler.conversion.loading.ProcessorClasspath
import spike.compiler.conversion.loading.WarmLoaderSlotPlatform
import spike.compiler.conversion.loading.WarmLoadersCounted
import spike.compiler.harness.CoProcessorJar
import spike.compiler.harness.IsolatedKsp
import spike.compiler.harness.Libraries
import spike.compiler.harness.SourceModule
import java.io.File
import java.lang.ref.WeakReference
import java.net.URLClassLoader
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertSame
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
            assertEquals(emptyList(), survivors(ksp.handed))
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
            assertEquals(emptyList(), survivors(ksp.handed))
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

    @Test
    fun `every per-run loader is collected after its run, the one that created the warm loader included`() {
        val work = File("build/tmp/compilations/warm_loader_per_run").apply { deleteRecursively() }
        val sources = File("src/test/resources/warm_loader/project/src/main")
        IsolatedKsp(SourceModule("main", sources, Libraries.Kotlin.files(), work)).use { ksp ->
            ksp.addToProcessorClasspath(CoProcessorJar.writeTo(work.resolve("co-processor.jar")))
            val creating = ksp.run()
            val warm = ksp.warmLoader()
            ksp.run()
            ksp.run()
            assertContains(creating, "Spike loaded the compiler classes")
            assertSame(warm, ksp.warmLoader())
            assertContains(ksp.perRunClasses.mapNotNull { it.get()?.name }, CoProcessorJar.ProviderName)
            assertEquals(emptyList(), survivors(ksp.perRunLoaders))
            assertEquals(emptyList(), survivors(ksp.perRunClasses))
        }
    }

    @Test
    fun `a co-processor crash leaves the warm loader usable by the next run`() {
        val work = File("build/tmp/compilations/warm_loader_after_crash").apply { deleteRecursively() }
        val sources = File("src/test/resources/warm_loader/project/src/main")
        val crashing = SourceModule("main", sources, Libraries.Kotlin.files(), work, mapOf(CoProcessorJar.CrashOption to "true"))
        IsolatedKsp(SourceModule("main", sources, Libraries.Kotlin.files(), work)).use { ksp ->
            ksp.addToProcessorClasspath(CoProcessorJar.writeTo(work.resolve("co-processor.jar")))
            ksp.run()
            assertContains(crashMessage(ksp, crashing), "The co-processor crashed")
            assertContains(ksp.run(), "Spike reused compiler classes that were already loaded")
        }
    }

    @Test
    fun `a co-processor crash in process leaves nothing behind once the loader is replaced`() =
        crashLeavesNothingBehind("crash_process", CoProcessorJar.CrashOption)

    @Test
    fun `a co-processor crash in create after Spike leased leaves nothing behind once the loader is replaced`() =
        crashLeavesNothingBehind("crash_create", CoProcessorJar.CreateCrashOption)

    @Test
    fun `a crash in a leased processor leaves nothing behind`() =
        crashLeavesNothingBehind("crash_leased", CoProcessorJar.LeasedCrashOption)

    @Test
    fun `a leased processor failing to start beside a co-processor keeping the environment in a static retains nothing`() =
        crashLeavesNothingBehind("crash_stash", CoProcessorJar.LeasedCreateCrashOption, CoProcessorJar.StashOption)

    @Test
    fun `one real run holds several leased processors on one loader`() {
        val work = File("build/tmp/compilations/warm_loader_siblings").apply { deleteRecursively() }
        val sources = File("src/test/resources/warm_loader/project/src/main")
        IsolatedKsp(SourceModule("main", sources, Libraries.Kotlin.files(), work)).use { ksp ->
            ksp.addToProcessorClasspath(CoProcessorJar.writeTo(work.resolve("co-processor.jar")))
            val output = ksp.run()
            assertEquals(1, Regex("Spike loaded the compiler classes").findAll(output).count())
            assertTrue(Regex("Spike reused compiler classes").findAll(output).count() >= 1)
            assertEquals(emptyList(), survivors(ksp.perRunLoaders))
        }
    }

    @Test
    fun `a per-run loader is collected while another run still holds the loader it replaced`() {
        val work = File("build/tmp/compilations/warm_loader_live_run").apply { deleteRecursively() }
        val sources = File("src/test/resources/warm_loader/project/src/main")
        val parent = ClassLoader.getPlatformClassLoader()
        val live = warmLoaders().lease(ProcessorClasspath(listOf(emptyJar(work).toURI().toURL()), parent), Any())
        IsolatedKsp(SourceModule("main", sources, Libraries.Kotlin.files(), work)).use { ksp ->
            ksp.run()
            assertEquals(emptyList(), survivors(ksp.perRunLoaders))
            assertNotNull((live.loader as URLClassLoader).findResource("empty.txt"))
        }
        live.release()
    }

    private fun crashLeavesNothingBehind(name: String, vararg options: String) {
        val work = File("build/tmp/compilations/warm_loader_$name").apply { deleteRecursively() }
        val sources = File("src/test/resources/warm_loader/project/src/main")
        val libraries = Libraries.Kotlin.files()
        val crashing = SourceModule("main", sources, libraries, work, options.associateWith { "true" })
        val jar = CoProcessorJar.writeTo(work.resolve("co-processor.jar"))
        val module = SourceModule("main", sources, libraries, work)
        IsolatedKsp(module).use { ksp ->
            ksp.addToProcessorClasspath(jar)
            ksp.run()
            val replaced = WeakReference(ksp.warmLoader())
            assertEquals(1, handlesOn(jar))
            assertContains(crashMessage(ksp, crashing), "The co-processor crashed")
            IsolatedKsp(module).use { it.run() }
            assertTrue(unloaded(replaced), "The replaced loader is still reachable")
            assertEquals(emptyList(), survivors(ksp.perRunLoaders))
            assertEquals(emptyList(), survivors(ksp.handed))
            assertTrue(eventually { handlesOn(jar) == 0 }, "The jar is still open ${handlesOn(jar)} times")
        }
    }

    private fun handlesOn(file: File): Int {
        val path = file.canonicalPath
        val descriptors = File("/proc/self/fd")
        if (descriptors.isDirectory) return descriptors.listFiles().orEmpty().count { descriptor ->
            runCatching { Files.readSymbolicLink(descriptor.toPath()).toString() }.getOrNull() == path
        }
        val process = ProcessBuilder("lsof", "-n", "-Fn", "-p", ProcessHandle.current().pid().toString()).start()
        return process.inputStream.bufferedReader().useLines { lines -> lines.count { it == "n$path" } }
    }

    private fun warmLoaders() = WarmLoadersCounted(WarmLoaderSlotPlatform(), SilentLog) {
        URLClassLoader(it.urls.toTypedArray(), it.parent)
    }

    private fun emptyJar(work: File) = work.resolve("empty.jar").apply {
        parentFile.mkdirs()
        ZipOutputStream(outputStream()).use { it.putNextEntry(ZipEntry("empty.txt")) }
    }

    private fun crashMessage(ksp: IsolatedKsp, module: SourceModule) =
        assertFails { ksp.run(module) }.cause?.message.orEmpty()

    private fun unloaded(reference: WeakReference<*>) = eventually { reference.get() == null }

    private fun <T : Any> survivors(references: List<WeakReference<out T>>): List<String> {
        eventually { references.all { it.get() == null } }
        return references.mapNotNull { it.get()?.toString() }
    }

    private fun eventually(condition: () -> Boolean): Boolean {
        val start = TimeSource.Monotonic.markNow()
        while (start.elapsedNow() < 10.seconds) {
            System.gc()
            if (condition()) return true
        }
        return false
    }

    private object SilentLog : KSPLogger {
        override fun logging(message: String, symbol: KSNode?) = Unit
        override fun info(message: String, symbol: KSNode?) = Unit
        override fun warn(message: String, symbol: KSNode?) = Unit
        override fun error(message: String, symbol: KSNode?) = Unit
        override fun exception(e: Throwable) = Unit
    }

}
