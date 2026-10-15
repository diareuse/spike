package spike.compiler.graph

import spike.compiler.diagnostic.DiagnosticDependencyCycle
import spike.compiler.diagnostic.DiagnosticException
import spike.compiler.diagnostic.Involved

data class TypeFactoryCreatorChain(
    override val type: Type,
    private val creators: List<TypeFactoryCreator>,
    override val store: GraphStore,
    override val isTopLevel: Boolean = true,
    private val chain: List<Type> = listOf(type)
) : TypeFactoryCreator.Context {

    override suspend fun Minting.resolve() = with(creators[0]) { create(this@TypeFactoryCreatorChain) }

    override suspend fun Minting.pass(creator: TypeFactoryCreator): TypeFactory {
        val index = creators.indexOf(creator) + 1
        if (index == creators.size) {
            val originatingElement = chain.asSequence()
                .take(chain.size - 1)
                .joinToString(" -> ")
                .ifEmpty { "<unknown origin>" }
            return TypeFactory.Imported(
                type = type,
                name = type.importName(),
                originatingElement = originatingElement,
                requiredBy = chain.getOrNull(chain.size - 2),
            )
        }
        return with(creators[index]) { create(this@TypeFactoryCreatorChain) }
    }

    override fun child(type: Type): TypeFactoryCreator.Context {
        if (type in chain) {
            val cycle = chain.dropWhile { it != type } + type
            val bindings = cycle.distinct().map {
                store.bindingsOf(it).firstOrNull()?.involved ?: Involved(it.displayName)
            }
            throw DiagnosticException(DiagnosticDependencyCycle(cycle.map { it.displayName }, bindings))
        }
        return copy(type = type, isTopLevel = false, chain = chain + type)
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
