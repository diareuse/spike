package spike.compiler.graph

interface EmptyMultibinding {
    suspend fun Minting.resolve(
        creator: TypeFactoryCreator,
        context: TypeFactoryCreator.Context,
        empty: TypeFactory,
    ): TypeFactory
}
