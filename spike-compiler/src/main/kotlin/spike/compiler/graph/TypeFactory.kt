package spike.compiler.graph

import java.util.Collections
import java.util.IdentityHashMap
import java.util.Objects

sealed interface TypeFactory {
    val type: Type
    val dependencies: List<TypeFactory>

    data class Class(
        override val type: Type,
        override val invocation: Invocation,
        override val singleton: Boolean,
        override val dependencies: List<TypeFactory>,
    ) : TypeFactory,
        Callable {
        private val hash = Objects.hash(type, invocation, singleton, dependencies)

        override fun hashCode() = hash

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
    }

    data class Method(
        override val type: Type,
        val member: Member.Method,
        override val invocation: Invocation,
        override val singleton: Boolean,
        override val dependencies: List<TypeFactory>,
    ) : TypeFactory,
        Callable {
        private val hash = Objects.hash(type, member, invocation, singleton, dependencies)

        override fun hashCode() = hash
    }

    data class Binds(
        override val type: Type,
        val source: TypeFactory,
    ) : TypeFactory {
        private val hash = Objects.hash(type, source)

        override fun hashCode() = hash

        override val dependencies: List<TypeFactory>
            get() = listOf(source)

        override fun toString(): String = "$source as $type"
    }

    sealed interface Deferred : TypeFactory {
        val factory: TypeFactory
    }

    data class Provides(
        override val type: Type.Parametrized,
        override val factory: TypeFactory,
    ) : Deferred {
        private val hash = Objects.hash(type, factory)

        override fun hashCode() = hash

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
    ) : Deferred {
        private val hash = Objects.hash(type, factory)

        override fun hashCode() = hash

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
        override val dependencies: List<TypeFactory>
            get() = emptyList()
    }

    data class External(
        override val type: Type,
        val name: String,
        val isMethod: Boolean,
        val origin: Type
    ) : TypeFactory {
        override val dependencies: List<TypeFactory>
            get() = emptyList()
    }

    class Imported(
        override val type: Type,
        val name: String,
        val originatingElement: String
    ): TypeFactory {
        override val dependencies: List<TypeFactory>
            get() = emptyList()

        override fun equals(other: Any?) = other is Imported && other.type == type && other.name == name
        override fun hashCode() = 31 * type.hashCode() + name.hashCode()
        override fun toString() = "Imported(type=$type, name=$name)"
    }

    data class MultibindsCollection(
        override val type: Type,
        val entries: List<TypeFactory>,
        val collectionType: Type,
    ) : TypeFactory {
        private val hash = Objects.hash(type, entries, collectionType)

        override fun hashCode() = hash

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
    }

    data class MultibindsMap(
        override val type: Type.Parametrized,
        val keyValues: Map<Any?, TypeFactory>,
    ) : TypeFactory {
        private val hash = Objects.hash(type, keyValues)

        override fun hashCode() = hash

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

    sealed interface Callable : TypeFactory {
        override val type: Type
        val invocation: Invocation
        val singleton: Boolean
        override val dependencies: List<TypeFactory>
    }

    companion object {
        operator fun List<TypeFactory>.contains(type: Type) = any { it.type == type }

        fun TypeFactory.invertDependencyTree(): List<TypeFactory> {
            val order = ArrayList<TypeFactory>()
            val visited = Collections.newSetFromMap(IdentityHashMap<TypeFactory, Boolean>())
            val pending = ArrayDeque<Pair<TypeFactory, Boolean>>()
            pending.addLast(this to false)
            while (pending.isNotEmpty()) {
                val (factory, expanded) = pending.removeLast()
                if (expanded) {
                    order += factory
                    continue
                }
                if (!visited.add(factory)) continue
                pending.addLast(factory to true)
                if (factory is Deferred) continue
                for (dependency in factory.dependencies) pending.addLast(dependency to false)
            }
            return order
        }
    }
}
