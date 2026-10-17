package spike.compiler.androidx

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

/**
 * Runs only when the processor option [key] is not set to false.
 */
class SymbolProcessorEnabledBy(
    private val options: Map<String, String>,
    private val key: String,
    private val delegate: SymbolProcessor,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (options[key]?.toBoolean() == false) return emptyList()
        return delegate.process(resolver)
    }
}
