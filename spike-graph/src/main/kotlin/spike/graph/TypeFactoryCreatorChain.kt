package spike.graph

data class TypeFactoryCreatorChain(
    override val type: Type,
    private val creators: List<TypeFactoryCreator>,
    override val store: GraphStore,
    override val isTopLevel: Boolean = true,
    private val index: Int = 0
) : TypeFactoryCreator.Context {

    override fun pass(): TypeFactory {
        if (creators.size == index) error("Cannot find factory for $type")
        return with(creators[index]) { copy(index = index + 1).create() }
    }

    override fun mint(
        type: Type,
        context: TypeFactoryCreator.Context
    ): TypeFactory {
        context as TypeFactoryCreatorChain
        return with(creators[0]) { context.copy(type = type, index = 1, isTopLevel = false).create() }
    }

    override fun clone(store: GraphStore) = copy(store = store)
}