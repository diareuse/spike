package spike.compiler.graph

import java.util.IdentityHashMap
import spike.BindTarget
import spike.Include
import spike.compiler.Order

class TypeFactoryCreatorMemo(private val recursions: Recursions) : TypeFactoryCreator {
    private val minted = IdentityHashMap<GraphStore, HashMap<Type, TypeFactory>>()

    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val byType = minted.getOrPut(context.store) { HashMap() }
        byType[context.type]?.let { return it }
        return pass(context).also {
            byType[context.type] = it
            recursions.resolved(context.type, it)
        }
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(10)
        fun source(): TypeFactoryCreatorSource = TypeFactoryCreatorSource { state ->
            TypeFactoryCreatorMemo(state.recursions)
        }
    }
}
