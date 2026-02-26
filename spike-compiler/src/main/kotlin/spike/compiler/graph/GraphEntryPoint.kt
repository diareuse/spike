package spike.compiler.graph

@Suppress("DATA_CLASS_COPY_VISIBILITY_WILL_BE_CHANGED_WARNING", "DataClassPrivateConstructor")
data class GraphEntryPoint private constructor(
    val type: Type,
    val factory: Factory,
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

    data class Factory(
        val type: Type,
        val method: Member.Method,
        val isVirtual: Boolean = false
    )

    companion object {

        @JvmName("invokeWithNull")
        operator fun invoke(
            type: Type,
            factory: Factory?,
            properties: List<Member.Property>,
            methods: List<Member.Method>
        ) = GraphEntryPoint(
            type = type,
            factory = factory ?: type.defaultFactory(),
            properties = properties,
            methods = methods
        )

        private fun Type.defaultFactory(): Factory {
            val factoryType = Type.Simple(packageName, "${simpleName}Factory")
            return Factory(
                type = factoryType,
                method = Member.Method(
                    packageName = packageName,
                    name = "create",
                    returns = this,
                    parent = factoryType
                ),
                isVirtual = true
            )
        }
    }
}