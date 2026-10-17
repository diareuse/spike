package spike.compiler.graph

class TypeFactoryCreatorAlias : TypeFactoryCreator {
    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val alias = context.store.aliasOf(context.type) ?: return pass(context)
        return mint(context, alias.target)
    }
}
