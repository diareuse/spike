package spike.compiler.graph

class TypeFactoryCreatorUniqueBinding : TypeFactoryCreator {
    override fun TypeFactoryCreator.Context.create(): TypeFactory {
        val bindings = store.bindingsOf(type)
        check(bindings.size <= 1) {
            """Duplicate bindings for ${type.displayName}, Spike cannot pick one:
                |${bindings.joinToString("\n") { "  - ${it.description}" }}
                |
                |<description>
                |  Keep exactly one binding for ${type.displayName}, or tell them apart with a
                |  `@spike.Qualifier`-based annotation and request the qualified type.
                |</description>""".trimMargin()
        }
        return pass()
    }
}
