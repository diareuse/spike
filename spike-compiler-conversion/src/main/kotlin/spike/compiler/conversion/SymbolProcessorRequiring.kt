package spike.compiler.conversion

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

/**
 * Converts only when [required] is on the compile classpath, so a converter stays silent in a module that does not
 * use the framework it serves.
 */
class SymbolProcessorRequiring(
    private val required: String,
    private val delegate: SymbolProcessor,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (resolver.getClassDeclarationByName(resolver.getKSNameFromString(required)) == null) return emptyList()
        return delegate.process(resolver)
    }
}
