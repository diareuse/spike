package spike.compiler.graph

import spike.compiler.diagnostic.DiagnosticDuplicateMapKey
import spike.compiler.diagnostic.DiagnosticException
import spike.compiler.diagnostic.DiagnosticMapKeyType

class TypeFactoryCreatorMultiBindMap(
    private val multibinding: MultiBindingStore,
) : TypeFactoryCreator {
    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val type = context.type
        if (!(type is Type.Parametrized && type.envelope == BuiltInTypes.Map)) {
            return pass(context)
        }
        val (keyType, valueType) = type.typeArguments
        val instanceMap = multibinding.map[valueType.unwrapParametrized()].orEmpty()
        val instances = instanceMap
            .filterKeys { it.type == keyType }
            .mapKeys { it.key.value }
        if (instanceMap.isNotEmpty() && instances.isEmpty()) failOnKeyType(type, keyType, valueType, instanceMap)
        val keyValues = buildMap {
            for ((k, v) in instances) {
                val definitions = (v.constructors + v.factories).distinct()
                if (definitions.size > 1) {
                    throw DiagnosticException(
                        DiagnosticDuplicateMapKey(type.displayName, k.renderedKey(), definitions.map { it.involved })
                    )
                }
                val definition = definitions.singleOrNull()
                    ?: error("No definition found for $type whilst being multi-bound, this is however most likely a spike inference error.")
                val newType = mint(
                    context = context.clone(store = context.store.overriddenBy(definition.asGraphStore())),
                    type = definition.type.overrideType(valueType),
                )
                put(k, newType)
            }
        }
        return TypeFactory.MultibindsMap(
            type = type,
            keyValues = keyValues,
        )
    }

    private fun failOnKeyType(type: Type, keyType: Type, valueType: Type, entries: Map<Key, GraphStore>): Nothing =
        throw DiagnosticException(
            DiagnosticMapKeyType(
                map = type.displayName,
                value = valueType.unwrapParametrized().displayName,
                requestedKey = keyType.displayName,
                requestedKeySimpleName = keyType.simpleName,
                keys = entries.keys.map { it.type.displayName }.distinct(),
                entries = entries.values.flatMap { it.constructors + it.factories }.map { it.involved },
            )
        )

    private fun Any?.renderedKey(): String = if (this is String) "\"$this\"" else toString()

    private fun Type.overrideType(valueType: Type) = when (valueType) {
        is Type.Parametrized -> when (valueType.envelope) {
            BuiltInTypes.Provider,
            BuiltInTypes.Lazy,
                -> Type.Parametrized(valueType.envelope, listOf(this), false)
            else -> this
        }
        else -> this
    }

    private fun Type.unwrapParametrized() = when (this) {
        is Type.Parametrized -> when (envelope) {
            BuiltInTypes.Provider,
            BuiltInTypes.Lazy -> typeArguments.single()
            else -> this
        }
        else -> this
    }
}
