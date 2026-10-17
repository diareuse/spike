package spike.compiler.processor

import spike.compiler.diagnostic.DiagnosticException
import spike.compiler.diagnostic.DiagnosticReplacesNothing
import spike.compiler.graph.GraphStore

class ReplacementCheckBound : ReplacementCheck {
    override fun check(replacement: Replacement, production: GraphStore) {
        if (replacement.providedType == null || production.bindingsOf(replacement.replaced).isNotEmpty()) return
        val base = replacement.base.qualifiedName!!.asString()
        throw DiagnosticException(DiagnosticReplacesNothing(replacement.replacedName, base, replacement.involved))
    }
}
