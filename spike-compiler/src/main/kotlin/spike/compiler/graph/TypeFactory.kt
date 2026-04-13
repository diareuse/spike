package spike.compiler.graph

import kotlin.collections.iterator

sealed interface TypeFactory {
    val type: Type
    val dependencies: List<TypeFactory>
    val isPublic: Boolean

    val canInline: Boolean
        get() = true

    class Class(
        override val type: Type,
        override val invocation: Invocation,
        override val singleton: Boolean,
        override val dependencies: List<TypeFactory>,
        override val isPublic: Boolean,
    ) : TypeFactory,
        Callable {
        override val canInline: Boolean
            get() = !singleton

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
                    ?: "<error cannot resolve ${parameter.type}>"
                first = false
            }
            if (!first) out += "\n"
            out += ")"
            return out
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Class) return false

            if (type != other.type) return false

            return true
        }

        override fun hashCode(): Int {
            return type.hashCode()
        }

    }

    class Method(
        override val type: Type,
        val member: Member.Method,
        override val invocation: Invocation,
        override val singleton: Boolean,
        override val dependencies: List<TypeFactory>,
        override val isPublic: Boolean,
    ) : TypeFactory,
        Callable {
        override val canInline: Boolean
            get() = !singleton

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Method) return false

            if (type != other.type) return false

            return true
        }

        override fun hashCode(): Int {
            return type.hashCode()
        }
    }

    class Binds(
        override val type: Type,
        val source: TypeFactory,
        override val isPublic: Boolean,
    ) : TypeFactory {
        override val dependencies: List<TypeFactory>
            get() = listOf(source)
        override val canInline: Boolean
            get() = !isPublic && super.canInline

        override fun toString(): String = "$source as $type"
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Binds) return false

            if (type != other.type) return false

            return true
        }

        override fun hashCode(): Int {
            return type.hashCode()
        }

    }

    sealed interface Deferred : TypeFactory {
        val factory: TypeFactory
    }

    class Provides(
        override val type: Type.Parametrized,
        override val factory: TypeFactory,
        override val isPublic: Boolean,
    ) : Deferred {
        override val dependencies: List<TypeFactory>
            get() = factory.dependencies
        init {
            check(type.envelope == BuiltInTypes.Provider) {
                "Providing type must be Provider<T>, got $type"
            }
        }

        override fun toString(): String {
            var out = "Provider {\n"
            out += factory.toString().prependIndent()
            out += "\n}"
            return out
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Provides) return false

            if (type != other.type) return false

            return true
        }

        override fun hashCode(): Int {
            return type.hashCode()
        }
    }

    class Memorizes(
        override val type: Type.Parametrized,
        override val factory: TypeFactory,
        override val isPublic: Boolean,
    ) : Deferred {
        override val dependencies: List<TypeFactory>
            get() = factory.dependencies
        init {
            check(type.envelope == BuiltInTypes.Lazy) {
                "Memorizing type must be Lazy<T>, got $type"
            }
        }

        override fun toString(): String {
            var out = "lazy {\n"
            out += factory.toString().prependIndent()
            out += "\n}"
            return out
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Memorizes) return false

            if (type != other.type) return false

            return true
        }

        override fun hashCode(): Int {
            return type.hashCode()
        }
    }

    class Property(
        override val type: Type,
        val name: String,
    ) : TypeFactory {
        override val isPublic: Boolean
            get() = true
        override val dependencies: List<TypeFactory>
            get() = emptyList()

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Property) return false

            if (type != other.type) return false

            return true
        }

        override fun hashCode(): Int {
            return type.hashCode()
        }
    }

    class MultibindsCollection(
        override val type: Type,
        val entries: List<TypeFactory>,
        override val isPublic: Boolean,
        val collectionType: Type,
    ) : TypeFactory {
        override val dependencies: List<TypeFactory> get() = entries

        val collectionMemberFactory
            get() = when (collectionType) {
                BuiltInTypes.List -> BuiltInMembers.listOf
                BuiltInTypes.Set -> BuiltInMembers.setOf
                else -> error("Unsupported collection type: $collectionType")
            }

        override fun toString(): String {
            var out = "$collectionType {"
            for (dependency in dependencies) {
                out += "\n"
                out += dependency.toString().prependIndent()
            }
            out += "\n}"
            return out
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is MultibindsCollection) return false

            if (type != other.type) return false

            return true
        }

        override fun hashCode(): Int {
            return type.hashCode()
        }
    }

    class MultibindsMap(
        override val type: Type,
        val keyValues: Map<Any?, TypeFactory>,
        override val isPublic: Boolean,
    ) : TypeFactory {
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

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is MultibindsMap) return false

            if (type != other.type) return false

            return true
        }

        override fun hashCode(): Int {
            return type.hashCode()
        }

    }

    sealed interface Callable : TypeFactory {
        override val type: Type
        val invocation: Invocation
        val singleton: Boolean
        override val canInline: Boolean
        override val dependencies: List<TypeFactory>
    }

    companion object {
        operator fun List<TypeFactory>.contains(type: Type) = any { it.type == type }
    }
}
