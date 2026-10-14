package spike.compiler.graph

class TypeFactoryCreatorBinder : TypeFactoryCreator {
    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val type = context.type
        val binder = context.store.binders.find { it.type == type } ?: return pass(context)
        return TypeFactory.Binds(
            type = type,
            source = mint(context, binder.source),
        )
    }
}
