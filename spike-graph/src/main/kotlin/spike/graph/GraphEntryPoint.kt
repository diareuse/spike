package spike.graph

data class GraphEntryPoint(
    val type: Type,
    val properties: List<Member.Property>,
    val methods: List<Member.Method>
) {
    override fun toString(): String {
        var out = ""
        out += "$type {\n"
        if (properties.isNotEmpty()) {
            out += properties.joinToString("\n") { "val ${it.name}: ${it.returns}" }.prependIndent()
            out += "\n"
        }
        if (methods.isNotEmpty()) {
            out += methods.joinToString("\n") { "fun ${it.name}(): ${it.returns}" }.prependIndent()
            out += "\n"
        }
        out += "}"
        return out
    }

    fun isRootProperty(type: Type): Boolean {
        return properties.any { it.returns == type } || methods.any { it.returns == type }
    }
}