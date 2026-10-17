package spike.compiler.graph

import spike.Include
import spike.compiler.diagnostic.Involved

/**
 * Everything that resolving one entry point's graph shares between its creators.
 */
@Include
class GraphState(
    val entry: GraphEntryPoint,
    val root: GraphStore,
    val multibinding: MultiBindingStore,
    val recursions: Recursions,
    val imports: TypeFactoryCreatorImportCollector,
    val imported: ImportedContributions,
    val warnings: GraphWarnings,
) {
    val inputs = TypeFactoryCreatorInput(root, Involved("entry point ${entry.type.displayName}", entry.origin))
}
