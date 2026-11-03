package spike.compiler.processor.symbol

import spike.Include
import spike.compiler.graph.Binding
import spike.compiler.graph.Constructor
import spike.compiler.graph.DependencyGraph
import spike.compiler.graph.Factory
import spike.compiler.graph.TypeFactory
import spike.compiler.graph.beneath
import java.util.Collections
import java.util.IdentityHashMap

@Include
class UnresolvedReachedFactories : UnresolvedReached {
    override fun of(
        graph: DependencyGraph,
        unresolved: Map<Binding, List<UnresolvedCompiledType>>,
    ): List<UnresolvedCompiledType> {
        if (unresolved.isEmpty()) return emptyList()
        return graph.reached().mapNotNull { it.binding()?.let(unresolved::get) }.flatten().distinct().toList()
    }

    private fun TypeFactory.binding(): Binding? = when (this) {
        is TypeFactory.Class -> Constructor(type, invocation, singleton)
        is TypeFactory.Method -> Factory(type, member, invocation, singleton)
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
