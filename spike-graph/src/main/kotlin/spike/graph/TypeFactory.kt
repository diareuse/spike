package spike.graph

sealed class TypeFactory {
    abstract val type: Type
    abstract val dependencies: List<TypeFactory>

    data class Class(
        override val type: Type,
        val invocation: Invocation,
        val singleton: Boolean,
        override val dependencies: List<TypeFactory>
    ) : TypeFactory() {
        override fun toString(): String {
            var out = ""
            out += "$type("
            var first = true
            for (parameter in invocation.parameters) {
                if (!first) out += ","
                out += "\n"
                val parametrized = parameter.type as? Type.Parametrized
                val targetType = if(parametrized?.envelope == ProviderType || parametrized?.envelope == LazyType) {
                    parameter.type.typeArguments.single()
                } else {
                    parameter.type
                }
                out += dependencies.firstOrNull { dit -> dit.type == targetType }?.toString()?.prependIndent() ?: error("${parameter.type} cannot be resolved to factory")
                first = false
            }
            if (!first) out += "\n"
            out += ")"
            return out
        }
    }

    data class Method(
        override val type: Type,
        val member: Member.Method,
        val invocation: Invocation,
        val singleton: Boolean,
        override val dependencies: List<TypeFactory>
    ) : TypeFactory()

    data class Binds(
        override val type: Type,
        val source: TypeFactory
    ) : TypeFactory() {
        override val dependencies: List<TypeFactory>
            get() = listOf(source)

        override fun toString(): String {
            return "$source as $type"
        }
    }

    data class Provides(
        override val type: Type,
        val factory: TypeFactory
    ) : TypeFactory() {
        override val dependencies: List<TypeFactory>
            get() = factory.dependencies

        override fun toString(): String {
            var out = "Provider {\n"
            out += factory.toString().prependIndent()
            out += "\n}"
            return out
        }
    }
}