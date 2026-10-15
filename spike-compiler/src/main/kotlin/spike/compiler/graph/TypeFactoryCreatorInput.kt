package spike.compiler.graph

class TypeFactoryCreatorInput(private val root: GraphStore) : TypeFactoryCreator {
    private val inputs = mutableMapOf<Type, MutableList<Input>>()

    fun put(factory: TypeFactory, description: String) {
        inputs.getOrPut(factory.type) { mutableListOf() } += Input(factory, description)
    }

    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val candidates = inputs[context.type] ?: return pass(context)
        val bindings = context.store.bindingsOf(context.type)
        if (bindings.isNotEmpty() && bindings != root.bindingsOf(context.type)) return pass(context)
        if (bindings.isNotEmpty()) failOnInputConflict(context.type.displayName, candidates, bindings)
        if (candidates.size > 1) failOnRepeatedInput(context.type.displayName, candidates)
        return candidates.single().factory
    }

    private fun failOnInputConflict(subject: String, inputs: List<Input>, bindings: List<Binding>): Nothing = error(
        """$subject is both an input of the entry point and an @Include binding, Spike cannot pick one:
            |${(inputs.map { it.description } + bindings.map { it.description }).joinToString("\n") { "  - $it" }}
            |
            |<description>
            |  Remove the @Include binding of $subject, or stop passing $subject in, or tell them apart with a
            |  `@spike.Qualifier`-based annotation and request the qualified type.
            |</description>""".trimMargin()
    )

    private fun failOnRepeatedInput(subject: String, inputs: List<Input>): Nothing = error(
        """$subject is an input of the entry point more than once, Spike cannot pick one:
            |${inputs.joinToString("\n") { "  - ${it.description}" }}
            |
            |<description>
            |  Keep one factory parameter of type $subject, stop requesting $subject, or tell the inputs apart
            |  with a `@spike.Qualifier`-based annotation and request the qualified type.
            |</description>""".trimMargin()
    )

    private class Input(val factory: TypeFactory, val description: String)
}
