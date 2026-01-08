package spike.graph

sealed class Member {
    data class Property(val packageName: String, val name: String, val returns: Type) : Member()
    data class Method(val packageName: String, val name: String, val returns: Type, val parent: Type?) : Member()
    data class Receiver(val receiver: Type, val member: Member) : Member()
}