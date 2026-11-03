package spike.compiler.processor

import spike.compiler.graph.Binding
import spike.compiler.processor.symbol.UnresolvedCompiledType

class CompiledGraph(
    val context: GraphContext,
    val unresolved: Map<Binding, List<UnresolvedCompiledType>> = emptyMap(),
)
