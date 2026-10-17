package spike.compiler.conversion

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSNode
import java.lang.reflect.Proxy

inline fun <reified T> unused(): T = Proxy.newProxyInstance(
    T::class.java.classLoader,
    arrayOf(T::class.java),
) { proxy, method, arguments ->
    when (method.name) {
        "equals" -> proxy === arguments?.single()
        "hashCode" -> System.identityHashCode(proxy)
        "toString" -> T::class.java.simpleName
        else -> error("${method.name} is not expected to be called")
    }
} as T

class FakeSymbolProcessor(private val deferred: List<KSAnnotated> = emptyList()) : SymbolProcessor {
    var rounds = 0
        private set

    override fun process(resolver: Resolver): List<KSAnnotated> {
        rounds++
        return deferred
    }
}

class FakeProblemReporter : ProblemReporter {
    val reported = mutableListOf<ConversionProblem>()

    override fun report(problem: ConversionProblem) {
        reported += problem
    }
}

fun problem(code: String, node: KSNode = unused()) = ConversionProblem(code, "message of $code", node)

class FakeConversion : Conversion<KSAnnotated> {
    val converted = mutableListOf<KSAnnotated>()

    override fun convert(subject: KSAnnotated) {
        converted += subject
    }
}
