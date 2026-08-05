package spike.compiler.graph

sealed class Member {
    data class Property(
        val packageName: String,
        val name: String,
        val returns: Type,
        val parent: Type? = null,
        val synthetic: Boolean = false
    ) : Member()

    data class Method(
        val packageName: String,
        val name: String,
        val returns: Type,
        val parent: Type? = null,
        val parameters: List<Parameter> = emptyList(),
    ) : Member()

    data class Receiver(
        val receiver: Type,
        val member: Member,
    ) : Member()
}
