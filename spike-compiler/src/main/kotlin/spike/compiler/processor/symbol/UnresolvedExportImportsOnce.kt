package spike.compiler.processor.symbol

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.compiler.diagnostic.Diagnostic

class UnresolvedExportImportsOnce(private val delegate: UnresolvedExportImports) : UnresolvedExportImports {
    private var reported = false

    override fun of(resolver: Resolver, generated: List<KSClassDeclaration>): List<Diagnostic> {
        if (reported) return emptyList()
        reported = true
        return delegate.of(resolver, generated)
    }
}
