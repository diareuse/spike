package spike.compiler.processor

import spike.compiler.diagnostic.DiagnosticException
import spike.compiler.diagnostic.DiagnosticReplacesType
import spike.BindTarget
import spike.Include
import spike.compiler.Order

@Include
class ReplacementCheckAssignable : ReplacementCheck {
    override fun check(replacement: Replacement, production: ProductionBindings) {
        val provided = replacement.providedType ?: return
        if (replacement.replacedType.isAssignableFrom(provided)) return
        throw DiagnosticException(
            DiagnosticReplacesType(replacement.replacedName, replacement.providedName, replacement.involved)
        )
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(10)
        val ReplacementCheckAssignable.asReplacementCheck: ReplacementCheck
            get() = this
    }
}
