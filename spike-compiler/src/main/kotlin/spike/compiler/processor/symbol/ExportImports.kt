package spike.compiler.processor.symbol

import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.compiler.diagnostic.Diagnostic

data class ExportImports(val diagnostics: List<Diagnostic>, val hidden: Map<String, Set<String>>) {
    fun hiddenIn(export: KSClassDeclaration) = hidden[export.qualifiedName?.asString().orEmpty()].orEmpty()
}
