package spike.compiler.graph

import java.util.IdentityHashMap

class TypeFactoryCreatorMemo : TypeFactoryCreator {
    private val minted = IdentityHashMap<GraphStore, HashMap<Type, TypeFactory>>()

    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val byType = minted.getOrPut(context.store) { HashMap() }
        byType[context.type]?.let { return it }
        return pass(context).also { byType[context.type] = it }
    }
}
