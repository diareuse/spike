package spike.compiler.graph

import com.google.devtools.ksp.processing.KSPLogger

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
        private val logger: KSPLogger,
        private val imports: Sequence<TypeFactory.Class>,
    ) {

        private val cache = TypeFactoryCreatorCache()

        init {
            val factory = entry.factory
            for (p in factory.method.parameters) {
                cache.put(p.type, TypeFactory.Property(p.type, p.name))
            }
        }

        fun putExternal(external: TypeFactory) {
            cache.put(external.type, external)
        }

        fun create(): DependencyGraph {
            val importCollector = TypeFactoryCreatorImportCollector()
            val creators = listOf(
                importCollector,
                cache,
                TypeFactoryCreatorConstructor(),
                TypeFactoryCreatorMethod(),
                TypeFactoryCreatorBinder(),
                TypeFactoryCreatorLazy(),
                TypeFactoryCreatorProvider(),
                TypeFactoryCreatorMultiBindMap(multibinding, logger),
                TypeFactoryCreatorMultiBindCollection(multibinding, BuiltInTypes.Set),
                TypeFactoryCreatorMultiBindCollection(multibinding, BuiltInTypes.List),
            )
            val methods = entry.methods.map {
                createTypeFactory(it.returns, creators)
            }
            val properties = entry.properties.map {
                createTypeFactory(it.returns, creators)
            }
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
            val chain = TypeFactoryCreatorChain(type, creators, root)
            return chain.pass()
        }
    }

}
