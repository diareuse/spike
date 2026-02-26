package spike.compiler.graph

data class MultiBindingStore(
    val set: Map<Type, GraphStore>,
    val list: Map<Type, GraphStore>,
    val map: Map<Type, Map<Key, GraphStore>>
) {

    class Builder {

        private val setBindings = mutableMapOf<Type, GraphStore.Builder>()
        private val listBindings = mutableMapOf<Type, GraphStore.Builder>()
        private val mapBindings = mutableMapOf<Type, MutableMap<Key, GraphStore.Builder>>()

        fun addToSet(type: Type, graph: GraphStore.Builder.() -> Unit) = setBindings.getOrPut(type) { GraphStore.Builder() }.apply(graph)
        fun addToList(type: Type, graph: GraphStore.Builder.()->Unit) = listBindings.getOrPut(type) { GraphStore.Builder() }.apply(graph)
        fun addToMap(type: Type, key: Key, graph: GraphStore.Builder.()->Unit) = mapBindings.getOrPut(type) { mutableMapOf() }.getOrPut(key) { GraphStore.Builder() }.apply(graph)

        fun build() = MultiBindingStore(
            set = setBindings.mapValues { it.value.build() },
            list = listBindings.mapValues { it.value.build() },
            map = mapBindings.mapValues { it.value.mapValues { it.value.build() } }
        )
    }

}
