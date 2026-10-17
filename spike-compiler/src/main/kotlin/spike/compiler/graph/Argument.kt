package spike.compiler.graph

import spike.compiler.diagnostic.Origin

data class Argument(
    override val type: Type,
    val index: Int,
    val function: Type,
) : Binding {
    override val description: String
        get() = "argument ${index + 1} of ${function.displayName}"

    override val origin: Origin?
        get() = null

    override fun asGraphStore() = GraphStore(arguments = listOf(this))

    override fun retyped(type: Type) = copy(type = type)
}
