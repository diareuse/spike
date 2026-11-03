package spike.compiler.processor

import spike.compiler.graph.GraphStore
import spike.compiler.graph.Type

class ProductionBindings(val store: GraphStore, val exported: Set<Type>, val uses: List<CompiledUse>) {
    fun binds(type: Type) = type in exported || store.bindingsOf(type).isNotEmpty()

    fun usersOf(type: Type): List<CompiledUse> {
        val users = LinkedHashSet<CompiledUse>()
        val pending = ArrayDeque(listOf(type.toString()))
        while (pending.isNotEmpty()) {
            val used = pending.removeLast()
            for (use in uses) if (use.uses(used) && users.add(use)) pending.addLast(use.use.type)
        }
        return users.sortedBy { it.description }
    }

    fun without(type: Type) = ProductionBindings(store.without(type), exported - type, uses.filterNot { it.isOf(type) })
}
