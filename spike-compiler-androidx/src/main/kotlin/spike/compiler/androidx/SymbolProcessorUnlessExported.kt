package spike.compiler.androidx

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import spike.compiler.conversion.SymbolSource

/**
 * Skips a library module, recognised by its exported declarations; only the app owns the ViewModel entry point.
 */
class SymbolProcessorUnlessExported(
    private val exported: SymbolSource,
    private val delegate: SymbolProcessor,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (exported.symbols(resolver).isNotEmpty()) return emptyList()
        return delegate.process(resolver)
    }
}
