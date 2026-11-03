package spike.compiler.processor.symbol

import spike.Include
import spike.compiler.graph.Binding
import spike.compiler.graph.DependencyGraph

fun interface UnresolvedReached {
    fun of(graph: DependencyGraph, unresolved: Map<Binding, List<UnresolvedCompiledType>>): List<UnresolvedCompiledType>

    companion object {
        @Include
        val UnresolvedReachedFactories.reached: UnresolvedReached
            get() = this
    }
}
