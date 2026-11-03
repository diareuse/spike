package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.compiler.diagnostic.Origin
import spike.compiler.graph.GraphStore
import spike.compiler.graph.MultiBindingStore
import spike.compiler.graph.Type
import spike.Include
import spike.compiler.processor.symbol.UnresolvedCompiledType

fun interface EntryPointStores {
    fun of(context: GraphContext, entryPoint: KSClassDeclaration, exported: Set<Type>, uses: List<CompiledUse>): Stores

    companion object {
        @Include
        val EntryPointStoresDerived.stores: EntryPointStores
            get() = this
    }

    class Stores(
        val root: GraphStore,
        val multibinding: MultiBindingStore,
        val replacedExports: Set<Type>,
        val unresolved: Map<Origin, List<UnresolvedCompiledType>> = emptyMap(),
    )
}

@Include
class EntryPointStoresLocal : EntryPointStores {
    override fun of(
        context: GraphContext,
        entryPoint: KSClassDeclaration,
        exported: Set<Type>,
        uses: List<CompiledUse>,
    ) =
        EntryPointStores.Stores(context.builder.build(), context.multibind.build(), emptySet())
}
