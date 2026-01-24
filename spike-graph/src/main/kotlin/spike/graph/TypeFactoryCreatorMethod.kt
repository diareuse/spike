package spike.graph

class TypeFactoryCreatorMethod : TypeFactoryCreator {
    override fun TypeFactoryCreator.Context.create(): TypeFactory {
        val factory = store.factories.find { it.type == type } ?: return pass()
        return TypeFactory.Method(
            type = type,
            member = factory.member,
            invocation = factory.invocation,
            singleton = factory.singleton,
            dependencies = factory.invocation.parameters.map { mint(it.type) },
            isPublic = isTopLevel
        )
    }
}