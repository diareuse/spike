package spike.compiler.processor.symbol

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.Include
import spike.Singleton
import spike.compiler.diagnostic.Diagnostic

fun interface UnresolvedExportImports {
    fun of(resolver: Resolver, generated: List<KSClassDeclaration>): List<Diagnostic>

    companion object {
        @Include
        @Singleton
        fun imports(core: UnresolvedExportImportsConstructor): UnresolvedExportImports =
            UnresolvedExportImportsOnce(core)
    }
}
