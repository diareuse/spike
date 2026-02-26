package spike.compiler.graph

class TypeFactoryCreatorBinder : TypeFactoryCreator {
    override fun TypeFactoryCreator.Context.create(): TypeFactory {
        val type = type
        val binder = store.binders.find { it.type == type } ?: return pass()
        return TypeFactory.Binds(
            type = type,
            source = mint(binder.source),
            isPublic = isTopLevel
        )
    }
}