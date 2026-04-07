package spike.compiler.graph

import spike.compiler.graph.GraphStore.Companion.asGraphStore
import spike.compiler.processor.env

class TypeFactoryCreatorMultiBindMap(
    private val multibinding: MultiBindingStore,
) : TypeFactoryCreator {
    override fun TypeFactoryCreator.Context.create(): TypeFactory {
        val type = type
        if (!(type is Type.Parametrized && type.envelope == BuiltInTypes.Map)) {
            return pass()
        }
        val (keyType, valueType) = type.typeArguments
        val instanceMap = checkNotNull(multibinding.map[valueType.unwrapParametrized()]) {
            "Multibinding for $type not found in ${multibinding.map}"
        }
        val instances = instanceMap
            .filterKeys { it.type == keyType }
            .mapKeys { it.key.value }
        if (instances.isEmpty())
            env.logger.warn("Wanted $keyType, but that was not present in ${instanceMap.map { it.key }}")
        val keyValues = buildMap {
            for ((k, v) in instances) {
                if (v.factories.isNotEmpty()) {
                    val definition = v.factories.singleOrNull()
                        ?: error("Multiple factories found for the same type $type. You must define only one per key ($k).")
                    put(k, mint(definition.type.overrideType(valueType), clone(store = definition.asGraphStore() + store)))
                    continue
                }
                if (v.constructors.isNotEmpty()) {
                    val definition = v.constructors.singleOrNull()
                        ?: error("Multiple classes found for the same $type. You must define only one per key ($k).")
                    put(k, mint(definition.type.overrideType(valueType), clone(store = definition.asGraphStore() + store)))
                    continue
                }
                if (v.binders.isNotEmpty()) {
                    val definition = v.binders.singleOrNull()
                        ?: error("Multiple bindings found for the same type $type. You must define only one per key ($k).")
                    put(k, mint(definition.type.overrideType(valueType), clone(store = definition.asGraphStore() + store)))
                    continue
                }
                error("No definition found for $type whilst being multi-bound, this is however most likely a spike inference error.")
            }
        }
        return TypeFactory.MultibindsMap(
            type = type,
            keyValues = keyValues,
            isPublic = isTopLevel,
        )
    }

    private fun Type.overrideType(valueType: Type) = when (valueType) {
        is Type.Parametrized -> when (valueType.envelope) {
            BuiltInTypes.Provider,
            BuiltInTypes.Lazy,
                -> Type.Parametrized(valueType.envelope, listOf(this))
            else -> this
        }
        else -> this
    }

    private fun Type.unwrapParametrized() = when (this) {
        is Type.Parametrized -> typeArguments.single()
        else -> this
    }
}
