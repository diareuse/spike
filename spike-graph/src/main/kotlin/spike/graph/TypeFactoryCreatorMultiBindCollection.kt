package spike.graph

class TypeFactoryCreatorMultiBindCollection(
    multibinding: MultiBindingStore,
    private val collectionType: TypeFactory.MultibindsCollection.Type
) : TypeFactoryCreator {
    private val envelope = when (collectionType) {
        TypeFactory.MultibindsCollection.Type.Set -> BuiltInTypes.Set
        TypeFactory.MultibindsCollection.Type.List -> BuiltInTypes.List
    }
    private val multibinding = when (collectionType) {
        TypeFactory.MultibindsCollection.Type.Set -> multibinding.set
        TypeFactory.MultibindsCollection.Type.List -> multibinding.list
    }

    override fun TypeFactoryCreator.Context.create(): TypeFactory {
        val type = type
        if (!(type is Type.Parametrized && type.envelope == envelope)) {
            return pass()
        }
        val valueType = type.typeArguments.single()
        val instances = multibinding[valueType]
            ?: error("Cannot find multibinding for $type, you must use spike.Include(bindTo = spike.BindTarget.List/Set) to use multibindings. Otherwise this type could not be found in graph.")
        return TypeFactory.MultibindsCollection(
            type = type,
            dependencies = buildList {
                addAll(instances.constructors.map { mint(it.type, clone(store = instances + store)) })
                addAll(instances.factories.map { mint(it.type, clone(store = instances + store)) })
                addAll(instances.binders.map { mint(it.type, clone(store = instances + store)) })
            },
            isPublic = isTopLevel,
            collectionType = collectionType
        )
    }
}