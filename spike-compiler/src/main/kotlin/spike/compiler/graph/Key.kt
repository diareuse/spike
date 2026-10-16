package spike.compiler.graph

class Key(
    val type: Type,
    val value: Any?,
    val annotation: Type? = null,
) {
    override fun equals(other: Any?) = other is Key && other.type == type && other.value == value

    override fun hashCode() = type.hashCode() * 31 + value.hashCode()

    override fun toString() = "Key(type=$type, value=$value)"
}
