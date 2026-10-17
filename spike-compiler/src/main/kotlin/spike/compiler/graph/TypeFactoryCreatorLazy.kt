package spike.compiler.graph

import spike.BindTarget
import spike.Include
import spike.compiler.Order

class TypeFactoryCreatorLazy(private val recursions: Recursions) : TypeFactoryCreator {
    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val type = context.type
        if (type is Type.Parametrized && type.envelope == BuiltInTypes.Lazy) {
            val target = type.typeArguments.single()
            val dependency = if (context.isResolving(target)) recursions.reference(target) else mint(context, target)
            return TypeFactory.Memorizes(type, dependency)
        }
        return pass(context)
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(90)
        fun source(): TypeFactoryCreatorSource = TypeFactoryCreatorSource { state ->
            TypeFactoryCreatorLazy(state.recursions)
        }
    }
}
