package spike.compiler.graph

import spike.compiler.graph.TypeFactory.Companion.contains

class TypeFactoryCreatorMultiBindCollection(
    multibinding: MultiBindingStore,
    private val collectionType: Type,
) : TypeFactoryCreator {
    private val multibinding = when (collectionType) {
        BuiltInTypes.Set -> multibinding.set
        BuiltInTypes.List -> multibinding.list
        else -> error("Unsupported collection type: $collectionType")
    }

    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val type = context.type
        val store = context.store
        if (!(type is Type.Parametrized && type.envelope == collectionType)) {
            return pass(context)
        }
        var valueType = type.typeArguments.single()
        if (valueType is Type.Parametrized && valueType.envelope == BuiltInTypes.Provider)
            valueType = valueType.typeArguments.single()
        val instances = multibinding[valueType] ?: GraphStore()
        return TypeFactory.MultibindsCollection(
            type = type,
            entries = buildList {
                for (c in instances.constructors) {
                    add(mint(context.clone(store = store.overriddenBy(c.asGraphStore())), c.type))
                }
                for (f in instances.factories) {
                    add(mint(context.clone(store = store.overriddenBy(f.asGraphStore())), f.type))
                }
                for (b in instances.binders) {
                    if (b.source in this) continue
                    add(mint(context.clone(store = store.overriddenBy(b.asGraphStore())), b.type))
                }
            },
            collectionType = collectionType,
        )
    }
}
