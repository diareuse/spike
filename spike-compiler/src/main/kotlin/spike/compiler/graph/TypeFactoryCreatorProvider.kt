package spike.compiler.graph

class TypeFactoryCreatorProvider : TypeFactoryCreator {
    override fun TypeFactoryCreator.Context.create(): TypeFactory {
        val type = type
        if (type is Type.Parametrized && type.envelope == BuiltInTypes.Provider) {
            val dependency = mint(type.typeArguments.single())
            return TypeFactory.Provides(type, dependency, isPublic = isTopLevel)
        }
        return pass()
    }
}