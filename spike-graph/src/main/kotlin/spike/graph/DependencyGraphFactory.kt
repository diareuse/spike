package spike.graph

import spike.graph.TypeFactory.MultibindsCollection.Type.List
import spike.graph.TypeFactory.MultibindsCollection.Type.Set

class DependencyGraphFactory(
    private val entry: GraphEntryPoint,
    private val root: GraphStore,
    private val multibinding: MultiBindingStore,
) {

    fun create(): DependencyGraph {
        val cache = TypeFactoryCreatorCache()
        val creators = listOf(
            cache,
            TypeFactoryCreatorConstructor(),
            TypeFactoryCreatorMethod(),
            TypeFactoryCreatorBinder(),
            TypeFactoryCreatorLazy(),
            TypeFactoryCreatorProvider(),
            TypeFactoryCreatorMultiBindMap(multibinding),
            TypeFactoryCreatorMultiBindCollection(multibinding, Set),
            TypeFactoryCreatorMultiBindCollection(multibinding, List),
        )
        val factory = entry.factory
        for (p in factory.method.parameters) {
            cache.put(p.type, TypeFactory.Property(p.type, p.name))
        }
        val methods = entry.methods.map {
            createTypeFactory(it.returns, creators)
        }
        val properties = entry.properties.map {
            createTypeFactory(it.returns, creators)
        }
        return DependencyGraph(
            entry = entry,
            methods = methods,
            properties = properties
        )
    }

    // ---

    private fun createTypeFactory(type: Type, creators: List<TypeFactoryCreator>): TypeFactory {
        val chain = TypeFactoryCreatorChain(type, creators, root)
        return chain.pass()
    }

}