package spike.compiler.graph

data class Qualifier(
    val type: Type,
    val arguments: List<Argument>
) : Comparable<Qualifier> {

    override fun compareTo(other: Qualifier): Int {
        if (this == other) return 0
        return toString().compareTo(other.toString())
    }

    override fun toString(): String {
        return "@" + type.toString() + arguments.joinToString(prefix = "(", postfix = ")")
    }

    data class Argument(
        val name: String,
        val value: Any?
    ) {
        override fun toString(): String {
            return "$name=$value"
        }
    }
}