package spike.compiler.conversion.loading

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import spike.compiler.conversion.FakeSymbolProcessor
import spike.compiler.conversion.unused
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertSame

class ProcessorSourceHostTest {

    private val logger = RecordingLogger()
    private val processor = FakeSymbolProcessor()
    private val cold = ProcessorSource { processor }

    @Test
    fun `a host that locks open jars goes straight to the cold source and says so`() {
        val untouched = ProcessorSource { error("the warm source must not run") }
        val source = ProcessorSourceHost(untouched, cold, HostSystem { true })
        assertSame(processor, source.processor(environment()))
        assertContains(logger.infos.single(), "Spike keeps its compiler classes to this run")
        assertEquals(emptyList(), logger.warnings)
    }

    @Test
    fun `a host that lets open jars be replaced uses the warm source`() {
        val warm = FakeSymbolProcessor()
        assertSame(warm, ProcessorSourceHost({ warm }, cold, HostSystem { false }).processor(environment()))
    }

    @Test
    fun `Windows locks open jars and other systems do not`() {
        assertEquals(true, HostSystemNamed("Windows 11").locksOpenFiles())
        assertEquals(false, HostSystemNamed("Mac OS X").locksOpenFiles())
        assertEquals(false, HostSystemNamed("Linux").locksOpenFiles())
    }

    private fun environment() = SymbolProcessorEnvironment(
        options = emptyMap(),
        kotlinVersion = KotlinVersion.CURRENT,
        codeGenerator = unused(),
        logger = logger,
    )
}
