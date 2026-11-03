package spike.compiler.processor.symbol

import spike.Include
import spike.compiler.diagnostic.Diagnostic
import spike.compiler.diagnostic.Origin

class UnresolvedFailure(val causes: List<UnresolvedCompiledType>, val others: List<Diagnostic>)

fun interface UnresolvedCauses {
    fun of(failure: List<Diagnostic>, unresolved: Map<Origin, List<UnresolvedCompiledType>>): UnresolvedFailure

    companion object {
        @Include
        val UnresolvedCausesInvolved.causes: UnresolvedCauses
            get() = this
    }
}
