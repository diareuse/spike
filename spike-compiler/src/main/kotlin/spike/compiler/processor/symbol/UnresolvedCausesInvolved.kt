package spike.compiler.processor.symbol

import spike.Include
import spike.compiler.diagnostic.Diagnostic
import spike.compiler.diagnostic.Origin

@Include
class UnresolvedCausesInvolved : UnresolvedCauses {
    override fun of(
        failure: List<Diagnostic>,
        unresolved: Map<Origin, List<UnresolvedCompiledType>>,
    ): UnresolvedFailure {
        val (caused, others) = failure.partition { diagnostic -> diagnostic.causes(unresolved).any() }
        val causes = caused.flatMap { it.causes(unresolved) }.distinct()
        return UnresolvedFailure(causes, others)
    }

    private fun Diagnostic.causes(unresolved: Map<Origin, List<UnresolvedCompiledType>>) =
        involved.mapNotNull { it.origin?.let(unresolved::get) }.flatten()
}
