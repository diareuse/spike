package spike.compiler.graph

import spike.BindTarget
import spike.Include
import spike.compiler.Order

class TypeFactoryCreatorAlias : TypeFactoryCreator {
    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val alias = context.store.aliasOf(context.type) ?: return pass(context)
        return mint(context, alias.target)
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(60)
        fun source(): TypeFactoryCreatorSource = TypeFactoryCreatorSource { TypeFactoryCreatorAlias() }
    }
}
