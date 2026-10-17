package spike.compiler.conversion

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated

class SymbolSourceFiltered(
    private val delegate: SymbolSource,
    private val keep: (KSAnnotated) -> Boolean,
) : SymbolSource {
    override fun symbols(resolver: Resolver): List<KSAnnotated> = delegate.symbols(resolver).filter(keep)
}
