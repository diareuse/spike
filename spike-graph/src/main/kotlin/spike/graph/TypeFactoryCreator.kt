package spike.graph

interface TypeFactoryCreator {
    fun Context.create(): TypeFactory
    interface Context {
        val type: Type
        val store: GraphStore
        val isTopLevel: Boolean
        //val multiBind: MultiBindingStore
        /** Passes execution to a lower level when this factory cannot provide the [TypeFactory] */
        fun pass(): TypeFactory

        /** Restarts the process from the start of the chain. This is useful for recursively finding types. */
        fun mint(type: Type, context: Context = this): TypeFactory
        fun clone(store: GraphStore = this.store): Context
    }
}
