package spike.compiler.processor.symbol

import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.Include

fun interface UnresolvedExportImports {
    fun of(generated: List<KSClassDeclaration>): List<UnresolvedCompiledType>

    companion object {
        @Include
        val UnresolvedExportImportsConstructor.imports: UnresolvedExportImports
            get() = this
    }
}
