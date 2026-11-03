package spike.compiler.processor.symbol

import spike.Include
import spike.compiler.diagnostic.Origin
import spike.compiler.graph.DependencyGraph

fun interface UnresolvedReached {
    fun of(graph: DependencyGraph, unresolved: Map<Origin, List<UnresolvedCompiledType>>): List<UnresolvedCompiledType>

    companion object {
        @Include
        val UnresolvedReachedFactories.reached: UnresolvedReached
            get() = this
    }
}
