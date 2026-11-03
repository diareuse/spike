package spike.compiler.processor.symbol

import spike.Include
import spike.compiler.diagnostic.Diagnostic
import spike.compiler.diagnostic.DiagnosticMissingBinding
import spike.compiler.diagnostic.Origin
import spike.compiler.graph.unresolvedNames

@Include
class UnresolvedCausesMissingBinding : UnresolvedCauses {
    override fun of(
        failure: List<Diagnostic>,
        unresolved: Map<Origin, List<UnresolvedCompiledType>>,
    ): UnresolvedFailure {
        val causes = failure.associateWith { it.causes(unresolved) }
        return UnresolvedFailure(
            causes = causes.values.flatten().distinct(),
            others = causes.filterValues { it.isEmpty() }.keys.toList(),
        )
    }

    private fun Diagnostic.causes(unresolved: Map<Origin, List<UnresolvedCompiledType>>): List<UnresolvedCompiledType> {
        if (this !is DiagnosticMissingBinding) return emptyList()
        val missing = this.missing.unresolvedNames
        return involved.mapNotNull { it.origin?.let(unresolved::get) }.flatten().filter { it.type in missing }
    }
}
