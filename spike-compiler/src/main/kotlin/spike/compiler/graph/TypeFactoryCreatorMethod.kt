package spike.compiler.graph

import spike.BindTarget
import spike.Include
import spike.compiler.Order

class TypeFactoryCreatorMethod : TypeFactoryCreator {
    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val factory = context.store.factoryOf(context.type) ?: return pass(context)
        return TypeFactory.Method(
            type = context.type,
            member = factory.member,
            invocation = factory.invocation,
            singleton = factory.singleton,
            dependencies = factory.invocation.parameters.map { mint(context, it.type) },
        )
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(80)
        fun source(): TypeFactoryCreatorSource = TypeFactoryCreatorSource { TypeFactoryCreatorMethod() }
    }
}
