package spike.compiler.processor

import spike.compiler.graph.GraphStore
import spike.compiler.graph.MultiBindingStore

data class GraphContext(
    val builder: GraphStore.Builder,
    val multibind: MultiBindingStore.Builder
)