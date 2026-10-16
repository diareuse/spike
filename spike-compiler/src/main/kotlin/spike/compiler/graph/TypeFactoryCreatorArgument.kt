package spike.compiler.graph

class TypeFactoryCreatorArgument : TypeFactoryCreator {
    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val argument = context.store.argumentOf(context.type) ?: return pass(context)
        return TypeFactory.Argument(argument.type, argument.index)
    }
}
