package spike.compiler.graph

import spike.compiler.graph.GraphStore.Companion.asGraphStore

class TypeFactoryCreatorMultiBindCollection(
    multibinding: MultiBindingStore,
    private val collectionType: Type
) : TypeFactoryCreator {
    private val multibinding = when (collectionType) {
        BuiltInTypes.Set -> multibinding.set
        BuiltInTypes.List -> multibinding.list
        else -> error("Unsupported collection type: $collectionType")
    }

    override fun TypeFactoryCreator.Context.create(): TypeFactory {
        val type = type
        if (!(type is Type.Parametrized && type.envelope == collectionType)) {
            return pass()
        }
        val valueType = type.typeArguments.single()
        val instances = multibinding[valueType] ?: GraphStore()
        return TypeFactory.MultibindsCollection(
            type = type,
            entries = buildList {
                addAll(instances.constructors.map { mint(it.type, clone(store = it.asGraphStore() + store)) })
                addAll(instances.factories.map { mint(it.type, clone(store = it.asGraphStore() + store)) })
                addAll(instances.binders.map { mint(it.type, clone(store = it.asGraphStore() + store)) })
            },
            isPublic = isTopLevel,
            collectionType = collectionType
        )
    }
}