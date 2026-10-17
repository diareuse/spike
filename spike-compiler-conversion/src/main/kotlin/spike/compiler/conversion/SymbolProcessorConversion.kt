package spike.compiler.conversion

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

class SymbolProcessorConversion(
    private val source: SymbolSource,
    private val check: ConversionCheck<KSAnnotated>,
    private val conversion: Conversion<KSAnnotated>,
    private val reporter: ProblemReporter,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        for (symbol in source.symbols(resolver)) {
            val problem = check.check(symbol)
            if (problem == null) conversion.convert(symbol) else reporter.report(problem)
        }
        return emptyList()
    }
}
