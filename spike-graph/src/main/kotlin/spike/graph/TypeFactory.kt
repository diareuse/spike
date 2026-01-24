package spike.graph

sealed class TypeFactory {
    abstract val type: Type
    abstract val dependencies: List<TypeFactory>
    abstract val isPublic: Boolean

    open val canInline: Boolean
        get() = true

    data class Class(
        override val type: Type,
        override val invocation: Invocation,
        override val singleton: Boolean,
        override val dependencies: List<TypeFactory>,
        override val isPublic: Boolean
    ) : TypeFactory(), Callable {
        override val canInline: Boolean
            get() = !singleton && super.canInline

        override fun toString(): String {
            var out = ""
            out += "$type("
            var first = true
            for (parameter in invocation.parameters) {
                if (!first) out += ","
                out += "\n"
                val parametrized = parameter.type as? Type.Parametrized
                val targetType = if (
                    parametrized?.envelope == BuiltInTypes.Provider ||
                    parametrized?.envelope == BuiltInTypes.Lazy
                ) {
                    parameter.type.typeArguments.single()
                } else {
                    parameter.type
                }
                out += dependencies.firstOrNull { dit -> dit.type == targetType }?.toString()?.prependIndent()
                    ?: error("${parameter.type} cannot be resolved to factory")
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
        override val invocation: Invocation,
        override val singleton: Boolean,
        override val dependencies: List<TypeFactory>,
        override val isPublic: Boolean
    ) : TypeFactory(), Callable {
        override val canInline: Boolean
            get() = !singleton && super.canInline
    }

    data class Binds(
        override val type: Type,
        val source: TypeFactory,
        override val isPublic: Boolean
    ) : TypeFactory() {
        override val dependencies: List<TypeFactory>
            get() = listOf(source)
        override val canInline: Boolean
            get() = !isPublic && super.canInline

        override fun toString(): String {
            return "$source as $type"
        }
    }

    data class Provides(
        override val type: Type,
        val factory: TypeFactory,
        override val isPublic: Boolean
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

    data class Property(
        override val type: Type,
        val name: String,
    ) : TypeFactory() {
        override val isPublic: Boolean
            get() = true
        override val dependencies: List<TypeFactory>
            get() = emptyList()
    }

    data class MultibindsCollection(
        override val type: spike.graph.Type,
        override val dependencies: List<TypeFactory>,
        override val isPublic: Boolean,
        val collectionType: Type
    ): TypeFactory() {
        enum class Type {
            Set, List
        }

        override fun toString(): String {
            var out = collectionType.name + " {"
            for (dependency in dependencies) {
                out += "\n"
                out += dependency.toString().prependIndent()
            }
            out += "\n}"
            return out
        }
    }

    data class MultibindsMap(
        override val type: Type,
        val keyValues: Map<Any?, TypeFactory>,
        override val isPublic: Boolean
    ): TypeFactory() {
        override val dependencies get() = keyValues.values.toList()
        override fun toString(): String {
            var out = "Map {"
            for ((key, value) in keyValues) {
                out += "\n"
                out += "$key -> $value"
            }
            out += "\n}"
            return out
        }
    }

    interface Callable {
        val type: Type
        val invocation: Invocation
        val singleton: Boolean
    }

    companion object {
        operator fun List<TypeFactory>.contains(type: Type) = any { it.type == type }
    }
}