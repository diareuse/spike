package spike.compiler.graph

import spike.compiler.diagnostic.Origin

data class Alias(
    override val type: Type,
    val target: Type,
    override val origin: Origin? = null,
) : Binding {
    override val description: String
        get() = "replacement of ${type.displayName} by ${target.displayName}"

    override fun asGraphStore() = GraphStore(aliases = listOf(this))

    override fun retyped(type: Type) = copy(type = type)
}
