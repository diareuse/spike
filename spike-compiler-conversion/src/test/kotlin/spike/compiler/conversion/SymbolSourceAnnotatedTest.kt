package spike.compiler.conversion

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import java.lang.reflect.Proxy
import kotlin.test.Test
import kotlin.test.assertEquals

class SymbolSourceAnnotatedTest {

    private val both = unused<KSAnnotated>()
    private val first = unused<KSAnnotated>()
    private val resolver = resolver(mapOf("a.First" to listOf(first, both), "a.Second" to listOf(both)))

    @Test
    fun `finds the symbols annotated with any of the names`() {
        val source = SymbolSourceAnnotated(AnnotationNames("a.First", "a.Second"))

        assertEquals(setOf(first, both), source.symbols(resolver).toSet())
    }

    @Test
    fun `finds a symbol annotated with two of the names once`() {
        val source = SymbolSourceAnnotated(AnnotationNames("a.First", "a.Second"))

        assertEquals(1, source.symbols(resolver).count { it === both })
    }

    private fun resolver(symbols: Map<String, List<KSAnnotated>>) = Proxy.newProxyInstance(
        Resolver::class.java.classLoader,
        arrayOf(Resolver::class.java),
    ) { _, method, arguments ->
        check(method.name == "getSymbolsWithAnnotation") { "${method.name} is not expected to be called" }
        symbols[arguments[0]].orEmpty().asSequence()
    } as Resolver
}
