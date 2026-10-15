package spike.compiler.graph

import spike.compiler.diagnostic.DiagnosticException
import spike.compiler.diagnostic.DiagnosticInputConflict
import spike.compiler.diagnostic.DiagnosticRepeatedInput
import spike.compiler.diagnostic.Involved
import spike.compiler.diagnostic.Origin

class TypeFactoryCreatorInput(
    private val root: GraphStore,
    private val entry: Involved? = null,
) : TypeFactoryCreator {
    private val inputs = mutableMapOf<Type, MutableList<Input>>()

    fun put(factory: TypeFactory, description: String, origin: Origin? = null) {
        inputs.getOrPut(factory.type) { mutableListOf() } += Input(factory, description, origin)
    }

    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val candidates = inputs[context.type]
        val bindings = if (candidates == null) emptyList() else context.store.bindingsOf(context.type)
        if (candidates == null || bindings.isNotEmpty() && bindings != root.bindingsOf(context.type)) {
            return pass(context)
        }
        if (bindings.isNotEmpty()) failOnInputConflict(context.type.displayName, candidates, bindings)
        if (candidates.size > 1) failOnRepeatedInput(context.type.displayName, candidates)
        return candidates.single().factory
    }

    private fun failOnInputConflict(subject: String, inputs: List<Input>, bindings: List<Binding>): Nothing =
        throw DiagnosticException(
            DiagnosticInputConflict(
                subject,
                inputs.map { it.involved },
                bindings.map { it.involved } + listOfNotNull(entry)
            )
        )

    private fun failOnRepeatedInput(subject: String, inputs: List<Input>): Nothing =
        throw DiagnosticException(DiagnosticRepeatedInput(subject, inputs.map { it.involved } + listOfNotNull(entry)))

    private class Input(val factory: TypeFactory, description: String, origin: Origin?) {
        val involved = Involved(description, origin)
    }
}
