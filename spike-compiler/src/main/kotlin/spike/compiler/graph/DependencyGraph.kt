package spike.compiler.graph

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
    }

    class Factory(
        private val entry: GraphEntryPoint,
        private val root: GraphStore,
        private val multibinding: MultiBindingStore,
        private val imports: Sequence<TypeFactory.Class>,
    ) {

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

        fun create(): DependencyGraph {
            val importCollector = TypeFactoryCreatorImportCollector()
            val creators = listOf(
                TypeFactoryCreatorMemo(),
                importCollector,
                inputs,
                TypeFactoryCreatorUniqueBinding(),
                TypeFactoryCreatorConstructor(),
                TypeFactoryCreatorMethod(),
                TypeFactoryCreatorLazy(),
                TypeFactoryCreatorProvider(),
                TypeFactoryCreatorMultiBindMap(multibinding),
                TypeFactoryCreatorMultiBindCollection(multibinding, BuiltInTypes.Set),
                TypeFactoryCreatorMultiBindCollection(multibinding, BuiltInTypes.List),
            )
            val methods = entry.methods.map {
                createTypeFactory(it.returns, creators)
            }
            val properties = entry.properties.map {
                createTypeFactory(it.returns, creators)
            }
            SharedSingletonInputCheck(root).check(entry, methods + properties)
            val imports = imports.toList()
            if (!entry.isModule && importCollector.imports.isNotEmpty()) {
                throw DiagnosticException(importCollector.imports.map { it.missing() })
            }
            return DependencyGraph(
                entry = entry,
                methods = methods,
                properties = properties,
                imports = imports,
                importFactories = importCollector.imports.toList()
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
