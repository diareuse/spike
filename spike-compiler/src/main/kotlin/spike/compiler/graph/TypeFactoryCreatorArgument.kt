package spike.compiler.graph

import spike.BindTarget
import spike.Include
import spike.compiler.Order

class TypeFactoryCreatorArgument : TypeFactoryCreator {
    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val argument = context.store.argumentOf(context.type) ?: return pass(context)
        return TypeFactory.Argument(argument.type, argument.index)
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(50)
        fun source(): TypeFactoryCreatorSource = TypeFactoryCreatorSource { TypeFactoryCreatorArgument() }
    }
}
