package spike.compiler.graph

import java.util.Collections
import java.util.IdentityHashMap
import java.util.Objects

sealed interface TypeFactory {
    val type: Type
    val dependencies: List<TypeFactory>

    sealed interface Structured : TypeFactory {
        fun pairedChildren(other: TypeFactory): List<Pair<TypeFactory, TypeFactory>>?
    }

    data class Class(
        override val type: Type,
        override val invocation: Invocation,
        override val singleton: Boolean,
        override val dependencies: List<TypeFactory>,
    ) : Structured,
        Callable {
        private val hash = Objects.hash(type, invocation, singleton, dependencies)

        override fun hashCode() = hash

        override fun equals(other: Any?) = structurallyEqual(this, other)

        override fun pairedChildren(other: TypeFactory) = dependencies.pairedWith(other.dependencies).takeIf {
            other is Class && other.type == type && other.invocation == invocation && other.singleton == singleton
        }

        override fun toString() = "$type$invocation"
    }

    data class Method(
        override val type: Type,
        val member: Member,
        override val invocation: Invocation,
        override val singleton: Boolean,
        override val dependencies: List<TypeFactory>,
    ) : Structured,
        Callable {
        private val hash = Objects.hash(type, member, invocation, singleton, dependencies)

        override fun hashCode() = hash

        override fun equals(other: Any?) = structurallyEqual(this, other)

        override fun pairedChildren(other: TypeFactory) = dependencies.pairedWith(other.dependencies).takeIf {
            other is Method && other.type == type && other.member == member && other.invocation == invocation &&
                other.singleton == singleton
        }

        override fun toString() = "$member$invocation: $type"
    }

    sealed interface Deferred : Structured {
        val factory: TypeFactory
    }

    data class Provides(
        override val type: Type.Parametrized,
        override val factory: TypeFactory,
    ) : Deferred {
        private val hash = Objects.hash(type, factory)

        override fun hashCode() = hash

        override fun equals(other: Any?) = structurallyEqual(this, other)

        override fun pairedChildren(other: TypeFactory) =
            if (other is Provides && other.type == type) listOf(factory to other.factory) else null

        override val dependencies: List<TypeFactory>
            get() = factory.dependencies
        init {
            check(type.envelope == BuiltInTypes.Provider) {
                "Providing type must be Provider<T>, got $type"
            }
        }

        override fun toString() = "$type"
    }

    data class Memorizes(
        override val type: Type.Parametrized,
        override val factory: TypeFactory,
    ) : Deferred {
        private val hash = Objects.hash(type, factory)

        override fun hashCode() = hash

        override fun equals(other: Any?) = structurallyEqual(this, other)

        override fun pairedChildren(other: TypeFactory) =
            if (other is Memorizes && other.type == type) listOf(factory to other.factory) else null

        override val dependencies: List<TypeFactory>
            get() = factory.dependencies
        init {
            check(type.envelope == BuiltInTypes.Lazy) {
                "Memorizing type must be Lazy<T>, got $type"
            }
        }

        override fun toString() = "$type"
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
        val originatingElement: String,
        val requiredBy: Type? = null,
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
    ) : Structured {
        private val hash = Objects.hash(type, entries, collectionType)

        override fun hashCode() = hash

        override fun equals(other: Any?) = structurallyEqual(this, other)

        override fun pairedChildren(other: TypeFactory) =
            if (other is MultibindsCollection && other.type == type && other.collectionType == collectionType) {
                entries.pairedWith(other.entries)
            } else {
                null
            }

        override val dependencies: List<TypeFactory> get() = entries

        val collectionMemberFactory
            get() = when (collectionType) {
                BuiltInTypes.List -> BuiltInMembers.listOf
                BuiltInTypes.Set -> BuiltInMembers.setOf
                else -> error("Unsupported collection type: $collectionType")
            }

        override fun toString() = "$collectionType of ${entries.map { it.type }}"
    }

    data class MultibindsMap(
        override val type: Type.Parametrized,
        val keyValues: Map<Any?, TypeFactory>,
    ) : Structured {
        private val hash = Objects.hash(type, keyValues)

        override fun hashCode() = hash

        override fun equals(other: Any?) = structurallyEqual(this, other)

        override fun pairedChildren(other: TypeFactory) =
            if (other is MultibindsMap && other.type == type && other.keyValues.keys == keyValues.keys) {
                keyValues.map { (key, value) -> value to other.keyValues.getValue(key) }
            } else {
                null
            }

        override val dependencies get() = keyValues.values.toList()
        override fun toString() = "Map of ${keyValues.mapValues { it.value.type }}"
    }

    sealed interface Callable : TypeFactory {
        override val type: Type
        val invocation: Invocation
        val singleton: Boolean
        override val dependencies: List<TypeFactory>
    }

    companion object {
        internal fun structurallyEqual(first: Structured, second: Any?): Boolean {
            if (second !is TypeFactory) return false
            val compared = HashSet<IdentityPair>()
            val pending = ArrayDeque<Pair<TypeFactory, TypeFactory>>()
            pending.addLast(first to second)
            var equal = true
            while (equal && pending.isNotEmpty()) {
                val (left, right) = pending.removeLast()
                val children = left.childrenPairedWith(right)
                equal = children != null
                if (!children.isNullOrEmpty() && compared.add(IdentityPair(left, right))) pending.addAll(children)
            }
            return equal
        }

        private fun TypeFactory.childrenPairedWith(other: TypeFactory): List<Pair<TypeFactory, TypeFactory>>? = when {
            this === other -> emptyList()
            hashCode() != other.hashCode() -> null
            this is Structured -> pairedChildren(other)
            this == other -> emptyList()
            else -> null
        }

        private fun List<TypeFactory>.pairedWith(other: List<TypeFactory>) =
            if (size == other.size) zip(other) else null

        fun TypeFactory.invertDependencyTree(): List<TypeFactory> {
            val order = ArrayList<TypeFactory>()
            val visited = Collections.newSetFromMap(IdentityHashMap<TypeFactory, Boolean>())
            val pending = ArrayDeque<Pair<TypeFactory, Boolean>>()
            pending.addLast(this to false)
            while (pending.isNotEmpty()) {
                val (factory, expanded) = pending.removeLast()
                when {
                    expanded -> order += factory
                    visited.add(factory) -> {
                        pending.addLast(factory to true)
                        if (factory !is Deferred) {
                            for (dependency in factory.dependencies) pending.addLast(dependency to false)
                        }
                    }
                }
            }
            return order
        }
    }

    private class IdentityPair(val left: TypeFactory, val right: TypeFactory) {
        override fun equals(other: Any?) = other is IdentityPair && other.left === left && other.right === right
        override fun hashCode() = 31 * System.identityHashCode(left) + System.identityHashCode(right)
    }
}
