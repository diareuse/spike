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
        val bindings = instances.constructors + instances.factories
        val (spreadBindings, elementBindings) = bindings.partition { it.type == type }
        val elements = buildList {
            for (binding in elementBindings) {
                add(mint(context.clone(store = store.overriddenBy(binding.asGraphStore())), binding.type))
            }
            addAll(imported.collection(target, valueType).map { it.wrappedAs(elementType) })
        }
        val spreads = buildList {
            for (binding in spreadBindings) {
                val spread = binding.retyped(Type.Qualified(binding.type, listOf(Elements)))
                add(mint(context.clone(store = store.overriddenBy(spread.asGraphStore())), spread.type))
            }
            addAll(imported.collection(target, type))
        }
        val collection = segments.of(
            TypeFactory.MultibindsCollection(type = type, entries = elements, collectionType = collectionType),
        )
        return if (spreads.isEmpty()) collection else TypeFactory.Concatenation(type, listOf(collection) + spreads)
    }
}

private val Elements = Qualifier(Type.Simple("spike", "Elements", false), emptyList())
