package spike.graph

class TypeFactoryCreatorLazy : TypeFactoryCreator {
    override fun TypeFactoryCreator.Context.create(): TypeFactory {
        val type = type
        if (type is Type.Parametrized && type.envelope == BuiltInTypes.Lazy) {
            return mint(type.typeArguments.single())
        }
        return pass()
    }
}