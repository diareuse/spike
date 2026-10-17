package spike.compiler.graph

import spike.BindTarget
import spike.Include
import spike.compiler.Order

class TypeFactoryCreatorUniqueBinding : TypeFactoryCreator {
    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val bindings = context.store.bindingsOf(context.type)
        if (bindings.size > 1) failOnDuplicateBindings(context.type.displayName, bindings)
        return pass(context)
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(40)
        fun source(): TypeFactoryCreatorSource = TypeFactoryCreatorSource { TypeFactoryCreatorUniqueBinding() }
    }
}
