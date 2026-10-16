package spike.compiler.graph

import spike.compiler.diagnostic.DiagnosticException
import spike.compiler.diagnostic.DiagnosticRepeatedArgument

class TypeFactoryCreatorAssisted(private val captors: List<ArgumentCaptor>) : TypeFactoryCreator {
    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val type = context.type
        if (!(type is Type.Parametrized && type.isFunction())) return pass(context)
        val arguments = type.typeArguments.dropLast(1)
        val result = type.typeArguments.last()
        val repeated = arguments.groupBy { it }.filterValues { it.size > 1 }.keys
        if (repeated.isNotEmpty()) {
            val involved = context.store.bindingsOf(result).map { it.involved }
            val argument = repeated.first().displayName
            throw DiagnosticException(DiagnosticRepeatedArgument(type.functionName, argument, involved))
        }
        val bound = GraphStore(arguments = arguments.mapIndexed { index, argument -> Argument(argument, index, type) })
        val factory = mint(context.clone(store = context.store + bound), result)
        ArgumentCaptureCheck(type.functionName, context.store, captors).check(factory)
        return TypeFactory.Assisted(type, factory)
    }

    private val Type.Parametrized.functionName: String
        get() = typeArguments.dropLast(1).joinToString(prefix = "(", postfix = ")") { it.displayName } +
            " -> " + typeArguments.last().displayName

    private fun Type.Parametrized.isFunction(): Boolean {
        val envelope = envelope as? Type.Simple ?: return false
        return envelope.packageName == "kotlin" && FunctionName.matches(envelope.simpleName) &&
            typeArguments.size > 1
    }

    private companion object {
        val FunctionName = Regex("Function\\d+")
    }
}
