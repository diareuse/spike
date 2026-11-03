package spike.compiler.processor

import spike.compiler.diagnostic.DiagnosticException
import spike.compiler.diagnostic.DiagnosticReplacesNothing
import spike.BindTarget
import spike.Include
import spike.compiler.Order

@Include
class ReplacementCheckBound : ReplacementCheck {
    override fun check(replacement: Replacement, production: ProductionBindings) {
        if (replacement.providedType == null || production.binds(replacement.replaced)) return
        val base = replacement.base.qualifiedName!!.asString()
        throw DiagnosticException(DiagnosticReplacesNothing(replacement.replacedName, base, replacement.involved))
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(20)
        val ReplacementCheckBound.asReplacementCheck: ReplacementCheck
            get() = this
    }
}
