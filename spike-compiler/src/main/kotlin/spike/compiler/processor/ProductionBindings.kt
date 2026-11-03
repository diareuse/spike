package spike.compiler.processor

import spike.compiler.graph.GraphStore
import spike.compiler.graph.Type

class ProductionBindings(val store: GraphStore, val exported: Set<Type>) {
    fun binds(type: Type) = type in exported || store.bindingsOf(type).isNotEmpty()

    fun without(type: Type) = ProductionBindings(store.without(type), exported - type)
}
