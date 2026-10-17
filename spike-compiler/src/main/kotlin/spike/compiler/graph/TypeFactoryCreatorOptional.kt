package spike.compiler.graph

import spike.compiler.diagnostic.DiagnosticUnboundOptional
import spike.BindTarget
import spike.Include
import spike.compiler.Order

class TypeFactoryCreatorOptional(
    private val inputs: TypeFactoryCreatorInput,
    private val warnings: GraphWarnings,
) : TypeFactoryCreator {
    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val type = context.type
        if (!type.nullable) return pass(context)
        val nonNull = type.nonNullable()
        val bound = context.store.bindingsOf(nonNull).isNotEmpty() || inputs.provides(nonNull) || nonNull.isCollection()
        return if (bound) mint(context, nonNull) else unbound(context, nonNull)
    }

    private fun unbound(context: TypeFactoryCreator.Context, nonNull: Type): TypeFactory {
        val requiredBy = context.requester?.let { context.store.bindingsOf(it).firstOrNull()?.involved }
        warnings.add(DiagnosticUnboundOptional(context.type.displayName, nonNull.displayName, requiredBy))
        return TypeFactory.Null(context.type)
    }

    private fun Type.isCollection() = this is Type.Parametrized &&
        envelope in listOf(BuiltInTypes.List, BuiltInTypes.Set, BuiltInTypes.Map)

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(40)
        fun source(): AppCreatorSource = AppCreatorSource { state ->
            TypeFactoryCreatorOptional(state.inputs, state.warnings)
        }
    }
}
