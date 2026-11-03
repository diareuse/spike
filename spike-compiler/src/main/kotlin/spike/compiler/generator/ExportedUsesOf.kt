package spike.compiler.generator

import spike.Include
import spike.compiler.graph.DependencyGraph
import spike.compiler.graph.ExportedContribution
import spike.compiler.graph.ExportedUse
import spike.compiler.graph.TypeFactory
import java.util.Collections
import java.util.IdentityHashMap

@Include
class ExportedUsesOf {
    fun of(graph: DependencyGraph): List<ExportedUse> = buildList {
        for (property in graph.entry.properties) add(use("property ${property.name}", graph.factoryOf(property)))
        for (method in graph.entry.methods) add(use("function ${method.name}", graph.factoryOf(method)))
        for (contribution in graph.contributions) {
            add(use(ExportedContribution.describe(contribution.name, contribution.target), contribution.factory))
        }
    }.filter { it.uses.isNotEmpty() }

    private fun use(binding: String, factory: TypeFactory) =
        ExportedUse(binding, factory.type.toString(), builtBelow(factory))

    private fun builtBelow(root: TypeFactory): Set<String> {
        val visited = Collections.newSetFromMap(IdentityHashMap<TypeFactory, Boolean>())
        val pending = ArrayDeque(root.beneath())
        val types = mutableSetOf<String>()
        while (pending.isNotEmpty()) {
            val factory = pending.removeLast()
            if (!visited.add(factory)) continue
            if (factory is TypeFactory.Callable || factory is TypeFactory.External) types += factory.type.toString()
            pending.addAll(factory.beneath())
        }
        return types
    }

    private fun TypeFactory.beneath(): List<TypeFactory> = when (this) {
        is TypeFactory.Assisted -> reached + factory
        is TypeFactory.Recursion -> listOfNotNull(target)
        else -> reached
    }
}
