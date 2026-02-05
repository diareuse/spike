package spike.graph

class DependencyGraph(
    val entry: GraphEntryPoint,
    val methods: List<TypeFactory>,
    val properties: List<TypeFactory>
) {

    operator fun iterator() = iterator {
        yieldAll(methods)
        yieldAll(properties)
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
