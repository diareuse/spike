package spike.graph

import kotlin.reflect.KClass

sealed class Type {
    abstract val packageName: String
    abstract val simpleName: String

    data class Simple(override val packageName: String, override val simpleName: String) : Type() {
        override fun toString(): String {
            return "$packageName.$simpleName"
        }
    }

    data class Inner(
        val parent: Type,
        override val simpleName: String
    ) : Type() {
        override val packageName: String get() = parent.packageName
        val names
            get() = buildList {
                var curr: Type = this@Inner
                while (curr is Inner) {
                    add(0, curr.simpleName)
                    curr = curr.parent
                }
                add(0, curr.simpleName)
            }

        override fun toString(): String {
            return "$parent.$simpleName"
        }
    }

    data class Parametrized(val envelope: Type, val typeArguments: List<Type>) : Type() {
        override val packageName: String
            get() = envelope.packageName
        override val simpleName: String
            get() = envelope.simpleName

        override fun toString(): String {
            return "$envelope<${typeArguments.joinToString(", ")}>"
        }
    }

    data class Qualified(val type: Type, val qualifiers: List<Qualifier>) : Type() {
        init {
            check(qualifiers.isNotEmpty()) { "At least one qualifier is required for type $type" }
        }

        override val packageName: String
            get() = type.packageName
        override val simpleName: String
            get() = type.simpleName

        override fun toString(): String {
            return "${qualifiers.joinToString(" ")} $type"
        }
    }

    companion object {
        operator fun invoke(klass: KClass<*>) = Simple(klass.qualifiedName!!.substringBefore(".${klass.simpleName!!}"), klass.simpleName!!)
    }
}