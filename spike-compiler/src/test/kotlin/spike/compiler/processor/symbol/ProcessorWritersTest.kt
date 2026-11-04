package spike.compiler.processor.symbol

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import java.lang.reflect.Proxy
import java.net.URLClassLoader
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProcessorWritersTest {

    private val resolver = unused<Resolver>()
    private val parent = ClassLoader.getPlatformClassLoader()

    @Test
    fun `a failure while discovering processors counts as a processor that may write`() {
        val failing = ProcessorWriters { throw java.util.ServiceConfigurationError("broken service file") }
        var logged = ""
        val logger = Proxy.newProxyInstance(javaClass.classLoader, arrayOf(KSPLogger::class.java)) { _, _, arguments ->
            logged = arguments.first().toString()
            Unit
        } as KSPLogger
        assertTrue(ProcessorWritersGuarded(failing, logger).mayWrite(resolver))
        assertContains(logged, "broken service file")
    }

    @Test
    fun `a discovery that succeeds is believed`() {
        assertFalse(ProcessorWritersGuarded({ false }, unused<KSPLogger>()).mayWrite(resolver))
    }

    @Test
    fun `Spike loaded beside KSP's API may have processors below it that it cannot see`() {
        assertTrue(ProcessorWritersSeparated({ false }, own = parent, api = parent).mayWrite(resolver))
    }

    @Test
    fun `Spike loaded below KSP's API sees the whole processor classpath`() {
        val own = URLClassLoader(emptyArray(), parent)
        assertFalse(ProcessorWritersSeparated({ false }, own = own, api = parent).mayWrite(resolver))
    }

    @Test
    fun `a processor that may write selects the settled path for every round`() {
        val immediate = CountingProcessor()
        val settled = CountingProcessor()
        val chosen = SymbolProcessorChosen({ true }, immediate, settled, unused<KSPLogger>())
        chosen.process(resolver)
        chosen.process(resolver)
        assertEquals(0, immediate.rounds)
        assertEquals(2, settled.rounds)
    }

    @Test
    fun `no processor that may write selects the immediate path once`() {
        var asked = 0
        val immediate = CountingProcessor()
        val logger = Proxy.newProxyInstance(javaClass.classLoader, arrayOf(KSPLogger::class.java)) { _, _, _ -> Unit }
        val chosen = SymbolProcessorChosen(
            writers = { asked++; false },
            immediate = immediate,
            settled = CountingProcessor(),
            logger = logger as KSPLogger,
        )
        chosen.process(resolver)
        chosen.process(resolver)
        assertEquals(1, asked)
        assertEquals(2, immediate.rounds)
    }

    private class CountingProcessor : SymbolProcessor {
        var rounds = 0

        override fun process(resolver: Resolver): List<KSAnnotated> {
            rounds++
            return emptyList()
        }
    }

    private inline fun <reified T> unused(): T =
        Proxy.newProxyInstance(T::class.java.classLoader, arrayOf(T::class.java)) { _, method, _ ->
            error("${method.name} is not expected to be called")
        } as T
}
