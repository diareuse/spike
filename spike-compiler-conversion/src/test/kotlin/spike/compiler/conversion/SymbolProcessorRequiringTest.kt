package spike.compiler.conversion

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSName
import java.lang.reflect.Proxy
import kotlin.test.Test
import kotlin.test.assertEquals

class SymbolProcessorRequiringTest {

    @Test
    fun `converts when the required class is on the classpath`() {
        val delegate = FakeSymbolProcessor()

        SymbolProcessorRequiring("a.Koin", delegate).process(resolver(available = "a.Koin"))

        assertEquals(1, delegate.rounds)
    }

    @Test
    fun `converts nothing when the required class is missing`() {
        val delegate = FakeSymbolProcessor()

        SymbolProcessorRequiring("a.Koin", delegate).process(resolver(available = "a.Other"))

        assertEquals(0, delegate.rounds)
    }

    private fun resolver(available: String) = Proxy.newProxyInstance(
        Resolver::class.java.classLoader,
        arrayOf(Resolver::class.java),
    ) { _, method, arguments ->
        when (method.name) {
            "getKSNameFromString" -> name(arguments[0] as String)
            "getClassDeclarationByName" ->
                if ((arguments[0] as KSName).asString() == available) unused<KSClassDeclaration>() else null
            else -> error("${method.name} is not expected to be called")
        }
    } as Resolver

    private fun name(value: String) = Proxy.newProxyInstance(
        KSName::class.java.classLoader,
        arrayOf(KSName::class.java),
    ) { _, method, _ ->
        check(method.name == "asString") { "${method.name} is not expected to be called" }
        value
    } as KSName
}
