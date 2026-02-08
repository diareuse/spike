package spike.graph

import spike.graph.GraphStore.Companion.asGraphStore

class TypeFactoryCreatorMultiBindMap(
    private val multibinding: MultiBindingStore
) : TypeFactoryCreator {
    override fun TypeFactoryCreator.Context.create(): TypeFactory {
        val type = type
        if (!(type is Type.Parametrized && type.envelope == BuiltInTypes.Map)) {
            return pass()
        }
        val (keyType, valueType) = type.typeArguments
        val instances = multibinding.map[valueType.unwrapParametrized()]
            ?.filterKeys { it.type == keyType }
            ?.mapKeys { it.key.value }
            .orEmpty()
        val keyValues = buildMap {
            for ((k, v) in instances) {
                var factory: TypeFactory? = null
                if (v.factories.isNotEmpty()) {
                    val definition = v.factories.singleOrNull()
                        ?: error("Multiple factories found for the same type $type. You must define only one per key ($k).")
                    factory = mint(definition.type.overrideType(valueType), clone(store = definition.asGraphStore() + store))
                }
                if (v.constructors.isNotEmpty()) {
                    if (factory != null) error("You cannot assign multiple contributors for the same key ($k) for the same type $type.")
                    val definition = v.constructors.singleOrNull()
                        ?: error("Multiple classes found for the same $type. You must define only one per key ($k).")
                    factory = mint(definition.type.overrideType(valueType), clone(store = definition.asGraphStore() + store))
                }
                if (v.binders.isNotEmpty()) {
                    if (factory != null) error("You cannot assign multiple contributors for the same key ($k) for the same type $type.")
                    val definition = v.binders.singleOrNull()
                        ?: error("Multiple bindings found for the same type $type. You must define only one per key ($k).")
                    factory = mint(definition.type.overrideType(valueType), clone(store = definition.asGraphStore() + store))
                }
                if (factory == null)
                    error("No definition found for $type whilst being multi-bound, this is however most likely a spike inference error.")
                put(k, factory)
            }
        }
        return TypeFactory.MultibindsMap(
            type = type,
            keyValues = keyValues,
            isPublic = isTopLevel
        )
    }

    private fun Type.overrideType(valueType: Type) = when (valueType) {
         is Type.Parametrized -> when (valueType.envelope) {
             BuiltInTypes.Provider,
             BuiltInTypes.Lazy -> Type.Parametrized(valueType.envelope, listOf(this))
             else -> this
         }
        else -> this
    }

    private fun Type.unwrapParametrized() = when (this) {
        is Type.Parametrized -> typeArguments.single()
        else -> this
    }
}