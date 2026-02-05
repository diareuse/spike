package spike.graph

sealed interface TypeFactory {
    val type: Type
    val dependencies: List<TypeFactory>
    val isPublic: Boolean

    val canInline: Boolean
        get() = true

    data class Class(
        override val type: Type,
        override val invocation: Invocation,
        override val singleton: Boolean,
        override val dependencies: List<TypeFactory>,
        override val isPublic: Boolean
    ) : TypeFactory, Callable {
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
    ) : TypeFactory, Callable {
        override val canInline: Boolean
            get() = !singleton
    }

    data class Binds(
        override val type: Type,
        val source: TypeFactory,
        override val isPublic: Boolean
    ) : TypeFactory {
        override val dependencies: List<TypeFactory>
            get() = listOf(source)
        override val canInline: Boolean
            get() = !isPublic && super.canInline

        override fun toString(): String {
            return "$source as $type"
        }
    }

    sealed interface Deferred : TypeFactory {
        val factory: TypeFactory
    }

    data class Provides(
        override val type: Type.Parametrized,
        override val factory: TypeFactory,
        override val isPublic: Boolean
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
    }

    data class Memorizes(
        override val type: Type.Parametrized,
        override val factory: TypeFactory,
        override val isPublic: Boolean
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
    }

    data class Property(
        override val type: Type,
        val name: String,
    ) : TypeFactory {
        override val isPublic: Boolean
            get() = true
        override val dependencies: List<TypeFactory>
            get() = emptyList()
    }

    data class MultibindsCollection(
        override val type: Type,
        val entries: List<TypeFactory>,
        override val isPublic: Boolean,
        val collectionType: Type
    ) : TypeFactory {
        // the values must be constructed locally as there can be a one binding and multibinding allows
        // one type bound multiple times
        override val dependencies: List<TypeFactory> = entries.flatMap { it.dependencies }

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
    }

    data class MultibindsMap(
        override val type: Type,
        val keyValues: Map<Any?, TypeFactory>,
        override val isPublic: Boolean
    ) : TypeFactory {
        // the values must be constructed locally as there can be a one binding and multibinding allows
        // one type bound multiple times
        override val dependencies get() = keyValues.values.flatMap { it.dependencies }.toList()
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

    sealed interface Callable: TypeFactory {
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