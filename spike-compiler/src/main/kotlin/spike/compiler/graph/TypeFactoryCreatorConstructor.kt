package spike.compiler.graph

class TypeFactoryCreatorConstructor : TypeFactoryCreator {
    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val constructor = context.store.constructors.find { it.type == context.type } ?: return pass(context)
        return TypeFactory.Class(
            type = context.type,
            invocation = constructor.invocation,
            singleton = constructor.singleton,
            dependencies = constructor.invocation.parameters.map { mint(context, it.type) },
        )
    }
}
