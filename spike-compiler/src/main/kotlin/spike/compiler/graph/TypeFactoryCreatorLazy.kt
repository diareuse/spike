package spike.compiler.graph

class TypeFactoryCreatorLazy : TypeFactoryCreator {
    override fun TypeFactoryCreator.Context.create(): TypeFactory {
        val type = type
        if (type is Type.Parametrized && type.envelope == BuiltInTypes.Lazy) {
            val dependency = mint(type.typeArguments.single())
            return TypeFactory.Memorizes(type, dependency, isPublic = isTopLevel)
        }
        return pass()
    }
}