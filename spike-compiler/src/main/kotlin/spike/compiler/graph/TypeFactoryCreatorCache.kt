package spike.compiler.graph

class TypeFactoryCreatorCache : TypeFactoryCreator {
    private val cache = mutableMapOf<Type, TypeFactory>()
    override fun TypeFactoryCreator.Context.create(): TypeFactory {
        return cache[type] ?: return pass()
    }

    fun put(type: Type, factory: TypeFactory) {
        cache[type] = factory
    }
}
