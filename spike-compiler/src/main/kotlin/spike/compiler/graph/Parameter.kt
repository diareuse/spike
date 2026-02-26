package spike.compiler.graph

data class Parameter(
    val name: String,
    val type: Type,
    val nullable: Boolean
) {
    override fun toString(): String {
        return "$name: $type" + if (nullable) "?" else ""
    }
}