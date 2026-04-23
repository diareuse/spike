package spike.compiler.graph

import com.google.devtools.ksp.processing.KSPLogger
import spike.compiler.graph.GraphStore.Companion.asGraphStore

class TypeFactoryCreatorMultiBindMap(
    private val multibinding: MultiBindingStore,
    private val logger: KSPLogger
) : TypeFactoryCreator {
    override fun TypeFactoryCreator.Context.create(): TypeFactory {
        val type = type
        if (!(type is Type.Parametrized && type.envelope == BuiltInTypes.Map)) {
            return pass()
        }
        val (keyType, valueType) = type.typeArguments
        var instanceMap = multibinding.map[valueType.unwrapParametrized()]
        if (instanceMap == null) {
            val unwrapped = valueType.unwrapParametrized()
            val keys = multibinding.map.keys
            logger.warn(
                "[soft-err] Multibinding for $type($unwrapped) not found in $keys. " +
                        "(This sometimes happens with Androidx ViewModels, nothing to worry about there)"
            )
            instanceMap = emptyMap()
        }
        val instances = instanceMap
            .filterKeys { it.type == keyType }
            .mapKeys { it.key.value }
        if (instances.isEmpty())
            logger.warn("Wanted $keyType, but that was not present in ${instanceMap.map { it.key }}")
        val keyValues = buildMap {
            for ((k, v) in instances) {
                if (v.factories.isNotEmpty()) {
                    val definition = v.factories.singleOrNull()
                        ?: error("Multiple factories found for the same type $type. You must define only one per key ($k).")
                    val newType = mint(
                        type = definition.type.overrideType(valueType),
                        context = clone(store = definition.asGraphStore() + store)
                    )
                    put(k, newType)
                } else if (v.constructors.isNotEmpty()) {
                    val definition = v.constructors.singleOrNull()
                        ?: error("Multiple classes found for the same $type. You must define only one per key ($k).")
                    val newType = mint(
                        type = definition.type.overrideType(valueType),
                        context = clone(store = definition.asGraphStore() + store)
                    )
                    put(k, newType)
                } else if (v.binders.isNotEmpty()) {
                    val definition = v.binders.singleOrNull()
                        ?: error("Multiple bindings found for the same type $type. You must define only one per key ($k).")
                    val newType = mint(
                        type = definition.type.overrideType(valueType),
                        context = clone(store = definition.asGraphStore() + store)
                    )
                    put(k, newType)
                } else
                    error("No definition found for $type whilst being multi-bound, this is however most likely a spike inference error.")
            }
        }
        return TypeFactory.MultibindsMap(
            type = type,
            keyValues = keyValues,
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
