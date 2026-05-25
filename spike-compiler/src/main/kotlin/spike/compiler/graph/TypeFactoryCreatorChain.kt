package spike.compiler.graph

data class TypeFactoryCreatorChain(
    override val type: Type,
    private val creators: List<TypeFactoryCreator>,
    override val store: GraphStore,
    override val isTopLevel: Boolean = true,
    private val chain: List<Type> = emptyList()
) : TypeFactoryCreator.Context {

    fun pass() = with(creators[0]) { create() }

    override fun pass(creator: TypeFactoryCreator): TypeFactory {
        val index = creators.indexOf(creator) + 1
        if (index == creators.size) {
            val originatingElement = chain.getOrNull(chain.size - 2)?.toString() ?: "<unknown origin>"
            error(
                """Client error, fix by adding element $type to the graph via @spike.Include:
                |<expected>
                |  @spike.Include
                |  class $type { /**/ }
                |</expected>
                |
                |<actual>
                |  Not Found
                |</actual>
                |
                |<description>
                |  `class $originatingElement(..., $type)` is declared somewhere in your application.
                |  $type couldn't be found in the graph. You may have forgotten to annotate with 
                |  a `@spike.Qualifier`-based annotation or `@spike.Include` is missing atop your class
                |</description>
            """.trimMargin()
            )
        }
        return with(creators[index]) { create() }
    }

    override fun mint(
        type: Type,
        context: TypeFactoryCreator.Context,
    ): TypeFactory {
        context as TypeFactoryCreatorChain
        if(type in context.chain) {
            val chain = chain.joinToString(separator = " -> ")
            error("Circular dependency detected: $chain")
        }
        return context.copy(type = type, isTopLevel = false, chain = context.chain + type).pass()
    }

    override fun clone(store: GraphStore) = copy(store = store)
}
