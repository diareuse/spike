package spike.compiler.graph

class TypeFactoryCreatorCache : TypeFactoryCreator {
    private val cache = mutableMapOf<Type, TypeFactory>()
    override fun TypeFactoryCreator.Context.create(): TypeFactory {
        return cache[type] ?: return pass(this@TypeFactoryCreatorCache)
    }

    fun put(type: Type, property: TypeFactory.Property) {
        cache[type] = property
    }
}
