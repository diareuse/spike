package spike.graph

import kotlin.collections.iterator

class TypeFactoryCreatorMultiBindMap(
    private val multibinding: MultiBindingStore
) : TypeFactoryCreator {
    override fun TypeFactoryCreator.Context.create(): TypeFactory {
        val type = type
        if (!(type is Type.Parametrized && type.envelope == BuiltInTypes.Map)) {
            return pass()
        }
        val (keyType, valueType) = type.typeArguments
        val instances = multibinding.map[valueType]
            ?.filterKeys { it.type == keyType }
            ?.mapKeys { it.key.value }
            ?: error("Cannot find multibinding for $type, you must use spike.Include(bindTo = spike.BindTarget.Map) to use multibindings. Otherwise this type could not be found in graph.")
        val keyValues = buildMap {
            for ((k, v) in instances) {
                var factory: TypeFactory? = null
                if (v.factories.isNotEmpty()) {
                    val factoryDefinition = v.factories.singleOrNull()
                        ?: error("Multiple factories found for the same type $type. You must define only one per key ($k).")
                    factory = mint(factoryDefinition.type, clone(store = v + store))
                }
                if (v.constructors.isNotEmpty()) {
                    if (factory != null) error("You cannot assign multiple contributors for the same key ($k) for the same type $type.")
                    val constructorDefinition = v.constructors.singleOrNull()
                        ?: error("Multiple classes found for the same $type. You must define only one per key ($k).")
                    factory = mint(constructorDefinition.type, clone(store = v + store))
                }
                if (v.binders.isNotEmpty()) {
                    if (factory != null) error("You cannot assign multiple contributors for the same key ($k) for the same type $type.")
                    val binderDefinition = v.binders.singleOrNull()
                        ?: error("Multiple bindings found for the same type $type. You must define only one per key ($k).")
                    factory = mint(binderDefinition.type, clone(store = v + store))
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
}