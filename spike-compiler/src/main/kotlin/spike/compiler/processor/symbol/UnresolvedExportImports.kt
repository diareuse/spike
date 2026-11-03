package spike.compiler.processor.symbol

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.Include
import spike.Singleton

fun interface UnresolvedExportImports {
    fun of(resolver: Resolver, generated: List<KSClassDeclaration>): ExportImports

    companion object {
        @Include
        @Singleton
        fun imports(core: UnresolvedExportImportsConstructor): UnresolvedExportImports =
            UnresolvedExportImportsOnce(core)
    }
}
