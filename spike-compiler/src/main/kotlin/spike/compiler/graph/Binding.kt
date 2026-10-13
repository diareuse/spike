package spike.compiler.graph

sealed interface Binding {
    val type: Type
    val description: String
    fun asGraphStore(): GraphStore
}

internal val Type.displayName: String
    get() = toString().removePrefix(".")

internal fun failOnDuplicateBindings(subject: String, bindings: List<Binding>): Nothing = error(
    """Duplicate bindings for $subject, Spike cannot pick one:
        |${bindings.joinToString("\n") { "  - ${it.description}" }}
        |
        |<description>
        |  Keep exactly one binding for $subject, or tell them apart with a
        |  `@spike.Qualifier`-based annotation and request the qualified type.
        |</description>""".trimMargin()
)
