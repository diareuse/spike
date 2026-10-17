package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.compiler.graph.GraphStore
import spike.compiler.graph.MultiBindingStore

fun interface EntryPointStores {
    fun of(context: GraphContext, entryPoint: KSClassDeclaration): Stores

    class Stores(val root: GraphStore, val multibinding: MultiBindingStore)
}

object EntryPointStoresLocal : EntryPointStores {
    override fun of(context: GraphContext, entryPoint: KSClassDeclaration) =
        EntryPointStores.Stores(context.builder.build(), context.multibind.build())
}
