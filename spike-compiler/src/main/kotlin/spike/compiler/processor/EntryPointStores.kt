package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.compiler.graph.GraphStore
import spike.compiler.graph.MultiBindingStore
import spike.compiler.graph.Type
import spike.Include

fun interface EntryPointStores {
    fun of(context: GraphContext, entryPoint: KSClassDeclaration, exported: Set<Type>): Stores

    companion object {
        @Include
        val EntryPointStoresDerived.stores: EntryPointStores
            get() = this
    }

    class Stores(
        val root: GraphStore,
        val multibinding: MultiBindingStore,
        val replacedExports: Set<Type>,
    )
}

@Include
class EntryPointStoresLocal : EntryPointStores {
    override fun of(context: GraphContext, entryPoint: KSClassDeclaration, exported: Set<Type>) =
        EntryPointStores.Stores(context.builder.build(), context.multibind.build(), emptySet())
}
