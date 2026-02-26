package spike.compiler.graph

class TypeFactoryHolder {

    private val factories = mutableMapOf<Type, TypeFactory>()

    fun insert(type: Type, factory: TypeFactory) {
        val ejected = factories.put(type, factory)
        if (ejected != null) error(
            "Cannot insert factory for $type, already exists: $ejected"
        )
    }

    operator fun get(type: Type): TypeFactory? {
        return factories[type]
    }

    companion object {

        inline fun TypeFactoryHolder.getOrPut(
            type: Type,
            factory: TypeFactoryHolder.(Type) -> TypeFactory
        ): TypeFactory {
            return get(type) ?: factory(type).also { insert(type, it) }
        }

    }

}