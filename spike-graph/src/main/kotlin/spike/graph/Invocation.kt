package spike.graph

data class Invocation(
    val parameters: List<Parameter>,
    val singleton: Boolean
) {

    override fun toString(): String {
        return parameters.joinToString(prefix = "(", postfix = ")") + (if (singleton) ".instance" else "")
    }

}