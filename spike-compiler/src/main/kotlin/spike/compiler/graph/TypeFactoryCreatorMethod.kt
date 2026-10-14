package spike.compiler.graph

class TypeFactoryCreatorMethod : TypeFactoryCreator {
    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val factory = context.store.factoryOf(context.type) ?: return pass(context)
        return TypeFactory.Method(
            type = context.type,
            member = factory.member,
            invocation = factory.invocation,
            singleton = factory.singleton,
            dependencies = factory.invocation.parameters.map { mint(context, it.type) },
        )
    }
}
