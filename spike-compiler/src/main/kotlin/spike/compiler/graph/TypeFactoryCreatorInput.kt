package spike.compiler.graph

class TypeFactoryCreatorInput(private val root: GraphStore) : TypeFactoryCreator {
    private val inputs = mutableMapOf<Type, Input>()

    fun put(factory: TypeFactory, description: String) {
        inputs[factory.type] = Input(factory, description)
    }

    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val input = inputs[context.type] ?: return pass(context)
        val bindings = context.store.bindingsOf(context.type)
        if (bindings.isEmpty()) return input.factory
        if (bindings == root.bindingsOf(context.type)) failOnInputConflict(context.type.displayName, input, bindings)
        return pass(context)
    }

    private fun failOnInputConflict(subject: String, input: Input, bindings: List<Binding>): Nothing = error(
        """$subject is both an input of the entry point and an @Include binding, Spike cannot pick one:
            |  - ${input.description}
            |${bindings.joinToString("\n") { "  - ${it.description}" }}
            |
            |<description>
            |  Remove the @Include binding of $subject, or stop passing $subject in, or tell them apart with a
            |  `@spike.Qualifier`-based annotation and request the qualified type.
            |</description>""".trimMargin()
    )

    private class Input(val factory: TypeFactory, val description: String)
}
