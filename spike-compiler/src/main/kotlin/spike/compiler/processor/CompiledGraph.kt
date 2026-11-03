package spike.compiler.processor

import spike.compiler.diagnostic.Origin
import spike.compiler.processor.symbol.UnresolvedCompiledType

class CompiledGraph(
    val context: GraphContext,
    val unresolved: Map<Origin, List<UnresolvedCompiledType>> = emptyMap(),
)
