package spike.compiler.graph

import spike.BindTarget
import spike.Include
import spike.compiler.Order

class TypeFactoryCreatorConstructor : TypeFactoryCreator {
    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val constructor = context.store.constructorOf(context.type) ?: return pass(context)
        return TypeFactory.Class(
            type = context.type,
            invocation = constructor.invocation,
            singleton = constructor.singleton,
            dependencies = constructor.invocation.parameters.map { mint(context, it.type) },
        )
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(70)
        fun source(): TypeFactoryCreatorSource = TypeFactoryCreatorSource { TypeFactoryCreatorConstructor() }
    }
}
