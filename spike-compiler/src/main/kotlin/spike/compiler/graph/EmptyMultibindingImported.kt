package spike.compiler.graph

object EmptyMultibindingImported : EmptyMultibinding {
    override suspend fun Minting.resolve(
        creator: TypeFactoryCreator,
        context: TypeFactoryCreator.Context,
        empty: TypeFactory,
    ) = with(context) { pass(creator) }
}
