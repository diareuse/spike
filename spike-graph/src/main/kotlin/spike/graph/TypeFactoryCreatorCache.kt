package spike.graph

class TypeFactoryCreatorCache: TypeFactoryCreator {
    private val cache = mutableMapOf<Type, TypeFactory>()
    override fun TypeFactoryCreator.Context.create(): TypeFactory {
        return cache.getOrPut(type) { pass() }
    }

    fun put(type: Type, property: TypeFactory.Property) {
        cache[type] = property
    }
}