package spike.compiler.conversion.loading

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import spike.compiler.conversion.FakeSymbolProcessor
import spike.compiler.conversion.unused
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ProcessorSourceTest {

    private val logger = RecordingLogger()
    private val processor = FakeSymbolProcessor()
    private val cold = ProcessorSource { processor }

    @Test
    fun `any warm failure falls back to the cold source and is logged without a warning`() {
        val failing = ProcessorSource { throw LinkageError("incompatible") }
        assertSame(processor, ProcessorSourceFallback(failing, cold).processor(environment()))
        assertContains(logger.infos.single(), "incompatible")
        assertEquals(emptyList(), logger.warnings)
    }

    @Test
    fun `a working warm source is used`() {
        val warm = FakeSymbolProcessor()
        assertSame(warm, ProcessorSourceFallback({ warm }, cold).processor(environment()))
    }

    @Test
    fun `spike warm false goes straight to the cold source`() {
        val untouched = ProcessorSource { error("the warm source must not run") }
        assertSame(processor, ProcessorSourceOption(untouched, cold).processor(environment("spike.warm" to "false")))
    }

    @Test
    fun `spike warm is on unless the option says otherwise`() {
        val warm = FakeSymbolProcessor()
        assertSame(warm, ProcessorSourceOption({ warm }, cold).processor(environment()))
        assertSame(warm, ProcessorSourceOption({ warm }, cold).processor(environment("spike.warm" to "true")))
    }

    @Test
    fun `the lease is released once KSP finishes the processor`() {
        var released = 0
        SymbolProcessorLeased(processor, lease(onRelease = { released++ })).finish()
        assertEquals(1, released)
    }

    @Test
    fun `the lease is released when KSP reports an error`() {
        var released = false
        SymbolProcessorLeased(processor, lease(onRelease = { released = true })).onError()
        assertTrue(released)
    }

    @Test
    fun `a processor that throws evicts its loader so the next build does not repeat the failure`() {
        var abandoned = false
        val lease = lease(onAbandon = { abandoned = true })
        val throwing = object : SymbolProcessor {
            override fun process(resolver: Resolver) = throw NoClassDefFoundError("Could not initialize class")
        }
        assertFailsWith<NoClassDefFoundError> { SymbolProcessorLeased(throwing, lease).process(unused()) }
        assertTrue(abandoned)
    }

    @Test
    fun `an error reported by KSP evicts the loader because it may be the cause`() {
        var evicted = false
        val lease = lease(onEvict = { evicted = true })
        SymbolProcessorLeased(processor, lease).onError()
        assertTrue(evicted)
    }

    private fun lease(onEvict: () -> Unit = {}, onAbandon: () -> Unit = {}, onRelease: () -> Unit = {}) =
        WarmLease(javaClass.classLoader, false, onEvict, onAbandon, onRelease)

    private fun environment(vararg options: Pair<String, String>) = SymbolProcessorEnvironment(
        options = mapOf(*options),
        kotlinVersion = KotlinVersion.CURRENT,
        codeGenerator = unused(),
        logger = logger,
    )
}
