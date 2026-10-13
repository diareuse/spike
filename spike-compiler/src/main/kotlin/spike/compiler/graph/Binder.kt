package spike.compiler.graph

data class Binder(
    val source: Type,
    override val type: Type,
) : Binding {
    override val description: String
        get() = "${source.displayName} bound as ${type.displayName}"

    override fun asGraphStore() = GraphStore(binders = listOf(this))
}
