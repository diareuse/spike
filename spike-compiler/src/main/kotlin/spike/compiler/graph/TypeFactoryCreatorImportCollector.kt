package spike.compiler.graph

import spike.Include
import spike.BindTarget
import spike.compiler.Order

@Include
class TypeFactoryCreatorImportCollector : TypeFactoryCreator {
    val imports: Set<TypeFactory.Imported>
        field = mutableSetOf()

    override suspend fun Minting.create(context: TypeFactoryCreator.Context) = pass(context).also { tf ->
        if (tf is TypeFactory.Imported)
            imports += tf
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(20)
        fun source(): TypeFactoryCreatorSource = TypeFactoryCreatorSource { state -> state.imports }
    }
}
