package spike.compiler.graph

data class Factory(
    override val type: Type,
    val member: Member,
    val invocation: Invocation,
    val singleton: Boolean,
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
}
