package spike.compiler.conversion

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

/**
 * Converts every symbol it finds, for declarations that Spike already validates itself.
 */
class SymbolProcessorUnchecked(
    private val source: SymbolSource,
    private val conversion: Conversion<KSAnnotated>,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        for (symbol in source.symbols(resolver)) conversion.convert(symbol)
        return emptyList()
    }
}
