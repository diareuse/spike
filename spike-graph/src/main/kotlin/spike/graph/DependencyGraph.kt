package spike.graph

class DependencyGraph(
    val entry: GraphEntryPoint,
    val methods: List<TypeFactory>,
    val properties: List<TypeFactory>
) {

    operator fun iterator() = iterator {
        val queue = mutableListOf<TypeFactory>()
        val emitted = mutableSetOf<Type>()
        queue.addAll(methods)
        queue.addAll(properties)
        while (queue.isNotEmpty()) {
            val item = queue.removeFirst()
            if (emitted.add(item.type)) {
                yield(item)
                queue.addAll(item.dependencies)
            }
        }
    }

    class Builder {

        private lateinit var store: GraphStore
        private lateinit var multibinding: MultiBindingStore

        fun addRootGraph(graph: GraphStore) = apply {
            this.store = graph
        }

        fun addMultibindGraph(graph: MultiBindingStore) = apply {
            this.multibinding = graph
        }

        fun build(entry: GraphEntryPoint): DependencyGraph {
            return DependencyGraphFactory(entry, store, multibinding).create()
        }

    }

}
