package spike.graph

data class Constructor(
    val type: Type,
    val invocation: Invocation,
    val singleton: Boolean
)
