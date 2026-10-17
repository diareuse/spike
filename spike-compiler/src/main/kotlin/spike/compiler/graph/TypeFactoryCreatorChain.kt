package spike.compiler.graph

import spike.compiler.diagnostic.DiagnosticDependencyCycle
import spike.compiler.diagnostic.DiagnosticException
import spike.compiler.diagnostic.Involved

data class TypeFactoryCreatorChain(
    override val type: Type,
    private val creators: List<TypeFactoryCreator>,
    override val store: GraphStore,
    override val isTopLevel: Boolean = true,
    private val chain: List<Type> = listOf(type),
    private val stores: List<GraphStore> = listOf(store),
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
        val repeated = chain.indices.firstOrNull { chain[it] == type && resolvesAlike(stores[it], type) }
        if (repeated != null) {
            val cycle = chain.drop(repeated) + type
            val bindings = cycle.distinct().map {
                store.bindingsOf(it).firstOrNull()?.involved ?: Involved(it.displayName)
            }
            throw DiagnosticException(DiagnosticDependencyCycle(cycle.map { it.displayName }, bindings))
        }
        return copy(type = type, isTopLevel = false, chain = chain + type, stores = stores + store)
    }

    /**
     * A type repeats as a cycle only when it resolves to the same binding again; a multibinding entry resolves its
     * own type through the entry's binding, which differs from a composite bound to the same type.
     */
    private fun resolvesAlike(earlier: GraphStore, type: Type) =
        earlier === store || earlier.bindingsOf(type).firstOrNull() == store.bindingsOf(type).firstOrNull()

    override fun clone(store: GraphStore) = copy(store = store)

    override fun isResolving(type: Type) = type in chain

    override val requester: Type?
        get() = chain.getOrNull(chain.size - 2)
}
