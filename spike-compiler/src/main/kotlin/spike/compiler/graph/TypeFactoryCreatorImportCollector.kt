package spike.compiler.graph

class TypeFactoryCreatorImportCollector : TypeFactoryCreator {
    val imports: Set<TypeFactory.Imported>
        field = mutableSetOf()

    override fun TypeFactoryCreator.Context.create() = pass().also { tf ->
        if (tf is TypeFactory.Imported)
            imports += tf
    }
}
