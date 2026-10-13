package spike.compiler.graph

data class Constructor(
    override val type: Type,
    val invocation: Invocation,
    val singleton: Boolean,
) : Binding {
    override val description: String
        get() = "constructor of ${type.displayName}"
}
