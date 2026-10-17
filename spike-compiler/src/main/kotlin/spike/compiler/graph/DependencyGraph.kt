package spike.compiler.graph

import spike.BindTarget
import spike.compiler.diagnostic.Diagnostic
import spike.compiler.diagnostic.DiagnosticException
import spike.compiler.diagnostic.DiagnosticMissingBinding
import spike.compiler.diagnostic.Involved

class DependencyGraph private constructor(
    val entry: GraphEntryPoint,
    val methods: List<TypeFactory>,
    val properties: List<TypeFactory>,
    val imports: List<TypeFactory.Class>,
    val importFactories: List<TypeFactory.Imported>,
    val contributions: List<ExportedContribution>,
    val warnings: List<Diagnostic> = emptyList(),
) {

    operator fun iterator() = iterator {
        yieldAll(methods)
        yieldAll(properties)
    }

    fun factoryOf(method: Member.Method): TypeFactory = methods[entry.methods.indexOf(method)]

    fun factoryOf(property: Member.Property): TypeFactory = properties[entry.properties.indexOf(property)]

    fun importedFactoryOf(type: Type): TypeFactory =
        properties[entry.properties.indexOfFirst { it.synthetic && it.returns == type }]

    fun toSequence() = sequence {
        for (m in methods)
            yield(m)
        for (p in properties)
            yield(p)
        for (c in contributions)
            yield(c.factory)
    }

    class Factory(
        private val entry: GraphEntryPoint,
        private val root: GraphStore,
        private val multibinding: MultiBindingStore,
        private val imports: Sequence<TypeFactory.Class>,
    ) {

        private val imported = ImportedContributions()

        private val inputs =
            TypeFactoryCreatorInput(root, Involved("entry point ${entry.type.displayName}", entry.origin))

        init {
            val factory = entry.factory
            for (p in factory.method.parameters) {
                val description = "parameter ${p.name} of ${factory.type.displayName}.${factory.method.name}"
                inputs.put(TypeFactory.Property(p.type, p.name), description, factory.origin)
            }
        }

        fun putExternal(external: TypeFactory.External, exportedBy: Type) {
            val kind = if (external.isMethod) "function" else "property"
            inputs.put(external, "$kind ${external.name} of @Export ${exportedBy.displayName}")
        }

        fun putContribution(target: BindTarget, key: Key?, external: TypeFactory.External, exportedBy: Type) {
            val description = ExportedContribution.describe(external.name, target)
            imported.put(target, key, external, Involved("$description, exported by ${exportedBy.displayName}"))
        }

        fun create(): DependencyGraph {
            val importCollector = TypeFactoryCreatorImportCollector()
            val recursions = Recursions()
            val warnings = mutableListOf<Diagnostic>()
            val resolving = listOf(
                TypeFactoryCreatorMemo(recursions),
                importCollector,
                inputs,
                TypeFactoryCreatorUniqueBinding(),
                TypeFactoryCreatorArgument(),
                TypeFactoryCreatorAlias(),
                TypeFactoryCreatorConstructor(),
                TypeFactoryCreatorMethod(),
                TypeFactoryCreatorLazy(recursions),
                TypeFactoryCreatorProvider(recursions),
                TypeFactoryCreatorAssisted(listOf(ArgumentCaptorSingleton(), ArgumentCaptorDeferred())),
            )
            val creators = entry.role.creators(resolving, multibinding, imported, inputs, warnings)
            val methods = entry.methods.map {
                createTypeFactory(it.returns, creators)
            }
            val properties = entry.properties.map {
                createTypeFactory(it.returns, creators)
            }
            val contributions = entry.role.contributions { exportContributions(creators) }
            entry.factory.ownership.inputCheck(root).check(entry, methods + properties)
            val imports = imports.toList()
            val unresolved = entry.role.unresolved(importCollector.imports.toList())
            if (unresolved.isNotEmpty()) throw DiagnosticException(unresolved.map { it.missing() })
            return DependencyGraph(
                entry = entry,
                methods = methods,
                properties = properties,
                imports = imports,
                importFactories = importCollector.imports.toList(),
                contributions = contributions,
                warnings = warnings.toList(),
            )
        }

        private fun exportContributions(creators: List<TypeFactoryCreator>) = buildList {
            for (store in multibinding.list.values) addAll(store.exported(BindTarget.List, null, creators))
            for (store in multibinding.set.values) addAll(store.exported(BindTarget.Set, null, creators))
            for ((valueType, keys) in multibinding.map) {
                for ((key, store) in keys) {
                    if (key.annotation != null) addAll(store.exported(BindTarget.Map, key, creators, valueType))
                }
            }
        }

        private fun GraphStore.exported(
            target: BindTarget,
            key: Key?,
            creators: List<TypeFactoryCreator>,
            exportedType: Type? = null,
        ) = (constructors + factories).map { binding ->
            val store = root.overriddenBy(binding.asGraphStore())
            val factory = minting(TypeFactoryCreatorChain(binding.type, creators, store))
            ExportedContribution(
                target = target,
                key = key,
                factory = factory,
                name = ExportedContribution.nameOf(target, binding),
                type = exportedType ?: factory.type,
            )
        }

        // ---

        private fun TypeFactory.Imported.missing(): Diagnostic {
            val requester = requiredBy?.let { root.bindingsOf(it).firstOrNull() }
            val requiredBy = requester?.involved ?: Involved("entry point ${entry.type.displayName}", entry.origin)
            return DiagnosticMissingBinding(type.displayName, type.simpleName, requiredBy)
        }

        private fun createTypeFactory(type: Type, creators: List<TypeFactoryCreator>): TypeFactory {
            return minting(TypeFactoryCreatorChain(type, creators, root))
        }

        private val minting = DeepRecursiveFunction<TypeFactoryCreator.Context, TypeFactory> { context ->
            with(context) { resolve() }
        }
    }

}
