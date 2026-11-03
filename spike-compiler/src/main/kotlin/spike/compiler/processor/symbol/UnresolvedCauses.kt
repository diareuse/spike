package spike.compiler.processor.symbol

import spike.Include
import spike.compiler.diagnostic.Diagnostic
import spike.compiler.diagnostic.Origin

fun interface UnresolvedCauses {
    fun of(failure: List<Diagnostic>, unresolved: Map<Origin, List<UnresolvedCompiledType>>): UnresolvedFailure

    companion object {
        @Include
        val UnresolvedCausesMissingBinding.causes: UnresolvedCauses
            get() = this
    }
}
