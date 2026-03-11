package spike.compiler.graph

data class TypeFactoryCreatorChain(
    override val type: Type,
    private val creators: List<TypeFactoryCreator>,
    override val store: GraphStore,
    override val isTopLevel: Boolean = true,
) : TypeFactoryCreator.Context {

    fun pass() = with(creators[0]) { create() }

    override fun pass(creator: TypeFactoryCreator): TypeFactory {
        val index = creators.indexOf(creator) + 1
        if (index == creators.size) error("Cannot find factory for $type in current context $store")
        return with(creators[index]) { create() }
    }

    override fun mint(
        type: Type,
        context: TypeFactoryCreator.Context,
    ): TypeFactory {
        context as TypeFactoryCreatorChain
        return context.copy(type = type, isTopLevel = false).pass()
    }

    override fun clone(store: GraphStore) = copy(store = store)
}
