package spike.compiler.graph

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

        private val inputs = TypeFactoryCreatorInput(root)

        init {
            val factory = entry.factory
            for (p in factory.method.parameters) {
                val description = "parameter ${p.name} of ${factory.type.displayName}.${factory.method.name}"
                inputs.put(TypeFactory.Property(p.type, p.name), description)
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
            SharedSingletonInputCheck().check(entry, methods + properties)
            val imports = imports.toList()
            if (!entry.isModule && importCollector.imports.isNotEmpty()) {
                val types = importCollector.imports.joinToString("\n") {
                    """<expected>
                    |  @spike.Include
                    |  class ${it.type} { /**/ }
                    |</expected>
                    |
                    |<actual>
                    |  Not Found
                    |</actual>
                    |
                    |<description>
                    |  `class ${it.originatingElement}(..., ${it.type})` is declared somewhere in your application.
                    |  ${it.type} couldn't be found in the graph. You may have forgotten to annotate with 
                    |  a `@spike.Qualifier`-based annotation or `@spike.Include` is missing atop your class
                    |</description>""".trimMargin()
                }
                error("Client error, your build is missing dependencies in graph:\n$types")
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

        private fun createTypeFactory(type: Type, creators: List<TypeFactoryCreator>): TypeFactory {
            return minting(TypeFactoryCreatorChain(type, creators, root))
        }

        private val minting = DeepRecursiveFunction<TypeFactoryCreator.Context, TypeFactory> { context ->
            with(context) { resolve() }
        }
    }

}
