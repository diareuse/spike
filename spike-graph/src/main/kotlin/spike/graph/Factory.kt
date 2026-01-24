package spike.graph

class Factory(
    val type: Type,
    val member: Member.Method,
    val invocation: Invocation,
    val singleton: Boolean
)