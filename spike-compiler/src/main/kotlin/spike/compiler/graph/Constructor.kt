package spike.compiler.graph

import spike.compiler.diagnostic.Origin

data class Constructor(
    override val type: Type,
    val invocation: Invocation,
    val singleton: Boolean,
    override val origin: Origin? = null,
) : Binding {
    override val description: String
        get() = "constructor of ${type.displayName}"

    override fun asGraphStore() = GraphStore(constructors = listOf(this))

    override fun equals(other: Any?) = other is Constructor &&
        other.type == type && other.invocation == invocation && other.singleton == singleton

    override fun hashCode() = (type.hashCode() * 31 + invocation.hashCode()) * 31 + singleton.hashCode()
}
