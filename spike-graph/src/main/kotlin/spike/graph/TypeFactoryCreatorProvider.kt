package spike.graph

class TypeFactoryCreatorProvider : TypeFactoryCreator {
    override fun TypeFactoryCreator.Context.create(): TypeFactory {
        val type = type
        if (type is Type.Parametrized && type.envelope == BuiltInTypes.Provider) {
            return mint(type.typeArguments.single())
        }
        return pass()
    }
}