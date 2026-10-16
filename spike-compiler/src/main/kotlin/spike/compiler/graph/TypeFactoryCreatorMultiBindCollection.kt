package spike.compiler.graph

import spike.BindTarget

class TypeFactoryCreatorMultiBindCollection(
    private val multibinding: Map<Type, GraphStore>,
    private val imported: ImportedContributions,
    private val collectionType: Type,
    private val target: BindTarget,
    private val segments: MultibindingSegments,
) : TypeFactoryCreator {

    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val type = context.type
        val store = context.store
        if (!(type is Type.Parametrized && type.envelope == collectionType)) {
            return pass(context)
        }
        val elementType = type.typeArguments.single()
        var valueType = elementType
        if (valueType is Type.Parametrized && valueType.envelope == BuiltInTypes.Provider)
            valueType = valueType.typeArguments.single()
        val instances = multibinding[valueType] ?: GraphStore()
        return segments.of(TypeFactory.MultibindsCollection(
            type = type,
            entries = buildList {
                for (c in instances.constructors) {
                    add(mint(context.clone(store = store.overriddenBy(c.asGraphStore())), c.type))
                }
                for (f in instances.factories) {
                    add(mint(context.clone(store = store.overriddenBy(f.asGraphStore())), f.type))
                }
                addAll(imported.collection(target, valueType).map { it.wrappedAs(elementType) })
            },
            collectionType = collectionType,
        ))
    }
}
