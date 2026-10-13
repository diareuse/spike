package spike.compiler.graph

data class Factory(
    override val type: Type,
    val member: Member.Method,
    val invocation: Invocation,
    val singleton: Boolean,
) : Binding {
    override val description: String
        get() {
            val owner = member.parent?.displayName ?: member.packageName
            return "function " + listOf(owner, member.name).filter { it.isNotEmpty() }.joinToString(".")
        }
}
