package spike.compiler.processor

import spike.graph.GraphStore
import spike.graph.MultiBindingStore

data class GraphContext(
    val builder: GraphStore.Builder,
    val multibind: MultiBindingStore.Builder
)