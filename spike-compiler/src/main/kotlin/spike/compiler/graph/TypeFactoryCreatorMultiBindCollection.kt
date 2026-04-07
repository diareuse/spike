package spike.compiler.graph

import spike.compiler.graph.GraphStore.Companion.asGraphStore
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

    override fun TypeFactoryCreator.Context.create(): TypeFactory {
        val type = type
        if (!(type is Type.Parametrized && type.envelope == collectionType)) {
            return pass()
        }
        var valueType = type.typeArguments.single()
        if (valueType is Type.Parametrized && valueType.envelope == BuiltInTypes.Provider)
            valueType = valueType.typeArguments.single()
        val instances = checkNotNull(multibinding[valueType]) {
            "Multibinding for $valueType($type) not found in $multibinding"
        }
        return TypeFactory.MultibindsCollection(
            type = type,
            entries = buildList {
                for (c in instances.constructors) {
                    add(mint(c.type, clone(store = c.asGraphStore() + store + instances)))
                }
                for (f in instances.factories) {
                    add(mint(f.type, clone(store = f.asGraphStore() + store + instances)))
                }
                for (b in instances.binders) {
                    if (b.source in this) continue
                    add(mint(b.type, clone(store = b.asGraphStore() + store + instances)))
                }
            },
            isPublic = isTopLevel,
            collectionType = collectionType,
        )
    }
}
