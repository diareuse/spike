package spike.compiler.graph

class TypeFactoryCreatorProvider : TypeFactoryCreator {
    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val type = context.type
        if (type is Type.Parametrized && type.envelope == BuiltInTypes.Provider) {
            val dependency = mint(context, type.typeArguments.single())
            return TypeFactory.Provides(type, dependency)
        }
        return pass(context)
    }
}
