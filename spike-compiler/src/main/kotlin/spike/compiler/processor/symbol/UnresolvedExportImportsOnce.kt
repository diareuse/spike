package spike.compiler.processor.symbol

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration

class UnresolvedExportImportsOnce(private val delegate: UnresolvedExportImports) : UnresolvedExportImports {
    private var checked: ExportImports? = null

    override fun of(resolver: Resolver, generated: List<KSClassDeclaration>): ExportImports {
        checked?.let { return it.copy(diagnostics = emptyList()) }
        return delegate.of(resolver, generated).also { checked = it }
    }
}
