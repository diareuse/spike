package spike.compiler.processor.symbol

import spike.Include
import spike.compiler.diagnostic.Origin
import spike.compiler.graph.DependencyGraph
import spike.compiler.graph.TypeFactory
import spike.compiler.graph.beneath
import java.util.Collections
import java.util.IdentityHashMap

@Include
class UnresolvedReachedFactories : UnresolvedReached {
    override fun of(
        graph: DependencyGraph,
        unresolved: Map<Origin, List<UnresolvedCompiledType>>,
    ): List<UnresolvedCompiledType> {
        if (unresolved.isEmpty()) return emptyList()
        return graph.reached().mapNotNull { it.origin()?.let(unresolved::get) }.flatten().distinct().toList()
    }

    private fun TypeFactory.origin(): Origin? = when (this) {
        is TypeFactory.Class -> origin
        is TypeFactory.Method -> origin
        else -> null
    }

    private fun DependencyGraph.reached(): Sequence<TypeFactory> = sequence {
        val visited = Collections.newSetFromMap(IdentityHashMap<TypeFactory, Boolean>())
        val pending = ArrayDeque(toSequence().toList())
        while (pending.isNotEmpty()) {
            val factory = pending.removeLast()
            if (!visited.add(factory)) continue
            yield(factory)
            pending.addAll(factory.beneath)
        }
    }
}
