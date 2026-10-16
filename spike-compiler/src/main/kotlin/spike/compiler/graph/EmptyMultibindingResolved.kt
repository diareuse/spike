package spike.compiler.graph

object EmptyMultibindingResolved : EmptyMultibinding {
    override suspend fun Minting.resolve(
        creator: TypeFactoryCreator,
        context: TypeFactoryCreator.Context,
        empty: TypeFactory,
    ) = empty
}
