package spike.compiler.graph

import com.google.devtools.ksp.processing.KSPLogger

class DependencyGraph(
    val entry: GraphEntryPoint,
    val methods: List<TypeFactory>,
    val properties: List<TypeFactory>,
) {

    operator fun iterator() = iterator {
        yieldAll(methods)
        yieldAll(properties)
    }

    fun toSequence() = sequence {
        for (m in methods)
            yield(m)
        for (p in properties)
            yield(p)
    }

    class Builder(
        private val logger: KSPLogger
    ) {

        private lateinit var store: GraphStore
        private lateinit var multibinding: MultiBindingStore

        fun addRootGraph(graph: GraphStore) = apply {
            this.store = graph
        }

        fun addMultibindGraph(graph: MultiBindingStore) = apply {
            this.multibinding = graph
        }

        fun build(entry: GraphEntryPoint): DependencyGraph = DependencyGraphFactory(
            entry = entry,
            root = store,
            multibinding = multibinding,
            logger = logger
        ).create()
    }
}
