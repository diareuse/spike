package spike.compiler.graph

import spike.compiler.diagnostic.Origin

data class Factory(
    override val type: Type,
    val member: Member,
    val invocation: Invocation,
    val singleton: Boolean,
    override val origin: Origin? = null,
) : Binding {
    override val description: String
        get() = member.description

    private val Member.description: String
        get() = when (this) {
            is Member.Method -> "function " + qualifiedName(parent, packageName, name)
            is Member.Property -> "property " + qualifiedName(parent, packageName, name)
            is Member.Receiver -> "${member.description} on ${receiver.displayName}"
        }

    private fun qualifiedName(parent: Type?, packageName: String, name: String) =
        listOf(parent?.displayName ?: packageName, name).filter { it.isNotEmpty() }.joinToString(".")

    override fun asGraphStore() = GraphStore(factories = listOf(this))

    override fun retyped(type: Type) = copy(type = type)

    override fun equals(other: Any?) = other is Factory && other.type == type && other.member == member &&
        other.invocation == invocation && other.singleton == singleton

    override fun hashCode() =
        ((type.hashCode() * 31 + member.hashCode()) * 31 + invocation.hashCode()) * 31 + singleton.hashCode()
}
