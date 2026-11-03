package spike.compiler.processor.symbol

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.Include
import spike.Singleton
import spike.compiler.diagnostic.Diagnostic

data class ExportImports(val diagnostics: List<Diagnostic>, val hidden: Map<String, Set<String>>) {
    fun hiddenIn(export: KSClassDeclaration) = hidden[export.qualifiedName?.asString().orEmpty()].orEmpty()
}

fun interface UnresolvedExportImports {
    fun of(resolver: Resolver, generated: List<KSClassDeclaration>): ExportImports

    companion object {
        @Include
        @Singleton
        fun imports(core: UnresolvedExportImportsConstructor): UnresolvedExportImports =
            UnresolvedExportImportsOnce(core)
    }
}
