package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSFile
import spike.compiler.graph.GraphStore
import spike.compiler.graph.MultiBindingStore

data class GraphContext(
    val builder: GraphStore.Builder,
    val multibind: MultiBindingStore.Builder,
    val originatingFiles: MutableList<KSFile> = mutableListOf()
)
