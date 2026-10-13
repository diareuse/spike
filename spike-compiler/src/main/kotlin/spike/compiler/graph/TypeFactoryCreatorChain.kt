package spike.compiler.graph

data class TypeFactoryCreatorChain(
    override val type: Type,
    private val creators: List<TypeFactoryCreator>,
    override val store: GraphStore,
    override val isTopLevel: Boolean = true,
    private val chain: List<Type> = listOf(type)
) : TypeFactoryCreator.Context {

    fun pass() = with(creators[0]) { create() }

    override fun pass(creator: TypeFactoryCreator): TypeFactory {
        val index = creators.indexOf(creator) + 1
        if (index == creators.size) {
            val originatingElement = chain.asSequence()
                .take(chain.size - 1)
                .joinToString(" -> ")
                .ifEmpty { "<unknown origin>" }
            return TypeFactory.Imported(
                type = type,
                name = type.importName(),
                originatingElement = originatingElement
            )
        }
        return with(creators[index]) { create() }
    }

    override fun mint(
        type: Type,
        context: TypeFactoryCreator.Context,
    ): TypeFactory {
        context as TypeFactoryCreatorChain
        if (type in context.chain) {
            val cycle = context.chain.dropWhile { it != type } + type
            error("Circular dependency detected: ${cycle.joinToString(" -> ") { it.displayName }}")
        }
        return context.copy(type = type, isTopLevel = false, chain = context.chain + type).pass()
    }

    override fun clone(store: GraphStore) = copy(store = store)
}

private fun Type.importName(): String = toString()
    .split(Regex("[^A-Za-z0-9]+"))
    .filter { it.isNotEmpty() }
    .mapIndexed { index, part ->
        if (index == 0) part.replaceFirstChar { it.lowercase() } else part.replaceFirstChar { it.uppercase() }
    }
    .joinToString("")
