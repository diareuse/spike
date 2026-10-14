package spike.compiler.graph

class TypeFactoryCreatorUniqueBinding : TypeFactoryCreator {
    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val bindings = context.store.bindingsOf(context.type)
        if (bindings.size > 1) failOnDuplicateBindings(context.type.displayName, bindings)
        return pass(context)
    }
}
