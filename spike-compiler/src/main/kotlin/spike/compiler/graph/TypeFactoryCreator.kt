package spike.compiler.graph

import spike.compiler.graph.TypeFactoryCreator.Context

typealias Minting = DeepRecursiveScope<Context, TypeFactory>

interface TypeFactoryCreator {
    suspend fun Minting.create(context: Context): TypeFactory
    interface Context {
        val type: Type
        val store: GraphStore
        val isTopLevel: Boolean

        suspend fun Minting.resolve(): TypeFactory

        /** Passes execution to a lower level when this factory cannot provide the [TypeFactory] */
        suspend fun Minting.pass(creator: TypeFactoryCreator): TypeFactory

        fun child(type: Type): Context
        fun clone(store: GraphStore = this.store): Context
    }
}

context(caller: TypeFactoryCreator)
internal suspend fun Minting.pass(context: Context) = with(context) { pass(caller) }

/** Restarts the process from the start of the chain without growing the call stack. */
internal suspend fun Minting.mint(context: Context, type: Type) = callRecursive(context.child(type))
