package spike.compiler.graph

class TypeFactoryCreatorConstructor : TypeFactoryCreator {
    override fun TypeFactoryCreator.Context.create(): TypeFactory {
        val constructor = store.constructors.find { it.type == type } ?: return pass()
        return TypeFactory.Class(
            type = type,
            invocation = constructor.invocation,
            singleton = constructor.singleton,
            dependencies = constructor.invocation.parameters.map { mint(it.type) },
            isPublic = isTopLevel
        )
    }
}