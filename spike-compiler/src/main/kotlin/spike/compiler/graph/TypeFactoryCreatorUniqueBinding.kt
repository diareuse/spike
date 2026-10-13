package spike.compiler.graph

class TypeFactoryCreatorUniqueBinding : TypeFactoryCreator {
    override fun TypeFactoryCreator.Context.create(): TypeFactory {
        val bindings = store.bindingsOf(type)
        if (bindings.size > 1) failOnDuplicateBindings(type.displayName, bindings)
        return pass()
    }
}
