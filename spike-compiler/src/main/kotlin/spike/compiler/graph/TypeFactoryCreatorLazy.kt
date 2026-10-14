package spike.compiler.graph

class TypeFactoryCreatorLazy : TypeFactoryCreator {
    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val type = context.type
        if (type is Type.Parametrized && type.envelope == BuiltInTypes.Lazy) {
            val dependency = mint(context, type.typeArguments.single())
            return TypeFactory.Memorizes(type, dependency)
        }
        return pass(context)
    }
}
