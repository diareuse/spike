package spike.compiler.graph

class TypeFactoryCreatorImportCollector : TypeFactoryCreator {
    val imports: Set<TypeFactory.Imported>
        field = mutableSetOf()

    override suspend fun Minting.create(context: TypeFactoryCreator.Context) = pass(context).also { tf ->
        if (tf is TypeFactory.Imported)
            imports += tf
    }
}
