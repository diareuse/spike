package spike.compiler.graph

class TypeFactoryCreatorCache : TypeFactoryCreator {
    private val cache = mutableMapOf<Type, TypeFactory>()
    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        return cache[context.type] ?: return pass(context)
    }

    fun put(type: Type, factory: TypeFactory) {
        cache[type] = factory
    }
}
