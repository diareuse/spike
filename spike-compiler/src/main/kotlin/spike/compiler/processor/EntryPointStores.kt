package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.compiler.graph.GraphStore
import spike.compiler.graph.MultiBindingStore
import spike.Include

fun interface EntryPointStores {
    fun of(context: GraphContext, entryPoint: KSClassDeclaration): Stores

    companion object {
        @Include
        val EntryPointStoresDerived.stores: EntryPointStores
            get() = this
    }

    class Stores(val root: GraphStore, val multibinding: MultiBindingStore)
}

@Include
class EntryPointStoresLocal : EntryPointStores {
    override fun of(context: GraphContext, entryPoint: KSClassDeclaration) =
        EntryPointStores.Stores(context.builder.build(), context.multibind.build())
}
