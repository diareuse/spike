package spike.compiler.graph

import spike.compiler.diagnostic.DiagnosticDuplicateMapKey
import spike.compiler.diagnostic.DiagnosticException
import spike.compiler.diagnostic.DiagnosticMapKeyType

class TypeFactoryCreatorMultiBindMap(
    private val multibinding: MultiBindingStore,
    private val imported: ImportedContributions,
    private val segments: MultibindingSegments,
) : TypeFactoryCreator {
    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val type = context.type
        if (!(type is Type.Parametrized && type.envelope == BuiltInTypes.Map)) {
            return pass(context)
        }
        val (keyType, valueType) = type.typeArguments
        val instanceMap = multibinding.map[valueType.unwrapParametrized()].orEmpty()
        val importedMap = imported.map(valueType.unwrapParametrized())
        val keys = (instanceMap.keys + importedMap.keys).filter { it.type == keyType }
        if (keys.isEmpty() && (instanceMap.isNotEmpty() || importedMap.isNotEmpty())) {
            failOnKeyType(type, keyType, valueType, instanceMap)
        }
        val keyValues = buildMap {
            for (key in keys) {
                val definitions = instanceMap[key]?.let { (it.constructors + it.factories).distinct() }.orEmpty()
                val imports = importedMap[key].orEmpty()
                if (definitions.size + imports.size > 1) {
                    val involved = definitions.map { it.involved } + imports.map { it.involved }
                    throw DiagnosticException(
                        DiagnosticDuplicateMapKey(type.displayName, key.value.renderedKey(), involved)
                    )
                }
                val definition = definitions.singleOrNull()
                put(
                    key.value,
                    if (definition == null) imports.single().external.wrappedAs(valueType)
                    else mint(
                        context = context.clone(store = context.store.overriddenBy(definition.asGraphStore())),
                        type = definition.type.overrideType(valueType),
                    )
                )
            }
        }
        return segments.of(TypeFactory.MultibindsMap(type = type, keyValues = keyValues))
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
