package spike.compiler.conversion

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

/**
 * Reports every symbol of a kind that has no Spike counterpart.
 */
class SymbolProcessorRejecting(
    private val source: SymbolSource,
    private val code: String,
    private val message: String,
    private val reporter: ProblemReporter,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        for (symbol in source.symbols(resolver)) reporter.report(ConversionProblem(code, message, symbol))
        return emptyList()
    }
}
