package spike.compiler.conversion

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

class SymbolProcessorUnlessCompiled(
    private val name: String,
    private val whenCompiled: SymbolProcessor,
    private val delegate: SymbolProcessor,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val declaration = resolver.getClassDeclarationByName(resolver.getKSNameFromString(name))
        if (declaration != null && declaration.containingFile == null) return whenCompiled.process(resolver)
        return delegate.process(resolver)
    }
}
