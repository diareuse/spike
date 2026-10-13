package spike.compiler.graph

class SharedSingletonInputCheck {
    fun check(entry: GraphEntryPoint, roots: List<TypeFactory>) {
        if (!entry.factory.sharesSingletons) return
        val inputs = HashMap<TypeFactory, TypeFactory.Property?>()

        fun inputReachedFrom(factory: TypeFactory): TypeFactory.Property? {
            if (factory in inputs) return inputs[factory]
            val input = factory as? TypeFactory.Property
                ?: factory.children().map(::inputReachedFrom).firstOrNull { it != null }
            inputs[factory] = input
            if (input != null && factory is TypeFactory.Callable && factory.singleton) {
                failOnSingletonInput(entry, factory, input)
            }
            return input
        }

        roots.forEach(::inputReachedFrom)
    }

    private fun TypeFactory.children(): List<TypeFactory> =
        if (this is TypeFactory.Deferred) dependencies + factory else dependencies

    private fun failOnSingletonInput(entry: GraphEntryPoint, singleton: TypeFactory, input: TypeFactory): Nothing {
        val factory = entry.factory.type.displayName
        error(
            """@Singleton ${singleton.type.displayName} depends on ${input.type.displayName}, a per-instance input of $factory:
                |
                |<description>
                |  Every entry point created by $factory shares its singletons, so
                |  ${singleton.type.displayName} would keep the ${input.type.displayName} of whichever instance created it first.
                |  Remove @Singleton from ${singleton.type.displayName}, or stop it depending on ${input.type.displayName}.
                |</description>""".trimMargin()
        )
    }
}
