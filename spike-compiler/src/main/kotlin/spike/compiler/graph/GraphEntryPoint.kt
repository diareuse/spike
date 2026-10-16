package spike.compiler.graph

import spike.compiler.diagnostic.Origin

@Suppress("DATA_CLASS_COPY_VISIBILITY_WILL_BE_CHANGED_WARNING", "DataClassPrivateConstructor")
data class GraphEntryPoint private constructor(
    val type: Type,
    val factory: Factory,
    val properties: List<Member.Property>,
    val methods: List<Member.Method>,
    val role: GraphRole,
    val origin: Origin? = null,
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

    fun isRootProperty(type: Type): Boolean = properties.any { it.returns == type } || methods.any { it.returns == type }

    data class Factory(
        val type: Type,
        val method: Member.Method,
        val sharesSingletons: Boolean = false,
        val origin: Origin? = null,
    )

    companion object {

        @JvmName("invokeWithNull")
        operator fun invoke(
            type: Type,
            factory: Factory,
            properties: List<Member.Property>,
            methods: List<Member.Method>,
            role: GraphRole,
            origin: Origin? = null,
        ) = GraphEntryPoint(
            type = type,
            factory = factory,
            properties = properties,
            methods = methods,
            role = role,
            origin = origin,
        )

        fun Type.virtualFactory(parameters: List<Parameter>): Factory {
            val factoryType = Type.Simple(packageName, "${simpleName}Factory", false)
            return Factory(
                type = factoryType,
                method = Member.Method(
                    packageName = packageName,
                    name = "create",
                    returns = this,
                    parent = factoryType,
                    parameters = parameters
                ),
            )
        }
    }
}
