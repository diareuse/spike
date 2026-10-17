package spike.compiler.graph

import kotlin.reflect.KClass

sealed class Type {
    abstract val packageName: String
    abstract val simpleName: String
    abstract val nullable: Boolean

    abstract fun nonNullable(): Type

    /**
     * The element type that a contribution of this type adds to a [collection] multibinding: the argument of a
     * collection-returning contribution, keeping its qualifiers, or this type itself.
     */
    open fun elementOf(collection: Type): Type = this

    data class Simple(
        override val packageName: String,
        override val simpleName: String,
        override val nullable: Boolean,
    ) : Type() {
        override fun toString(): String = "$packageName.$simpleName${if (nullable) "?" else ""}"

        override fun nonNullable() = copy(nullable = false)
    }

    data class Inner(
        val parent: Type,
        override val simpleName: String,
        override val nullable: Boolean,
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

        override fun toString(): String = "$parent.$simpleName${if (nullable) "?" else ""}"

        override fun nonNullable() = copy(nullable = false)
    }

    data class Parametrized(
        val envelope: Type,
        val typeArguments: List<Type>,
        override val nullable: Boolean,
    ) : Type() {
        override val packageName: String
            get() = envelope.packageName
        override val simpleName: String
            get() = envelope.simpleName

        override fun toString(): String = "$envelope<${typeArguments.joinToString(", ")}>${if (nullable) "?" else ""}"

        override fun nonNullable() = copy(nullable = false)

        override fun elementOf(collection: Type) = if (envelope == collection) typeArguments.single() else this
    }

    data class WithVariance(
        val type: Type?,
        val variance: Variance,
    ) : Type() {
        override val packageName: String
            get() = type?.packageName.orEmpty()
        override val simpleName: String
            get() = type?.simpleName.orEmpty()
        override val nullable: Boolean
            get() = type?.nullable == true

        enum class Variance {
            IN,
            OUT,
            STAR,
            ;

            override fun toString() = when (this) {
                IN -> "in"
                OUT -> "out"
                STAR -> "*"
            }
        }

        override fun toString() = when (type) {
            null -> "$variance"
            else -> "$variance $type"
        }

        override fun nonNullable() = this
    }

    data class Qualified(
        val type: Type,
        val qualifiers: List<Qualifier>
    ) : Type() {
        init {
            check(qualifiers.isNotEmpty()) { "At least one qualifier is required for type $type" }
        }

        override val packageName: String
            get() = type.packageName
        override val simpleName: String
            get() = type.simpleName
        override val nullable: Boolean
            get() = type.nullable

        override fun toString(): String = "${qualifiers.joinToString(" ")} $type"

        override fun nonNullable() = copy(type = type.nonNullable())

        override fun elementOf(collection: Type): Type {
            val element = type.elementOf(collection)
            return if (element === type) this else Qualified(element, qualifiers)
        }
    }

    companion object {
        operator fun invoke(klass: KClass<*>, nullable: Boolean) = Simple(
            packageName = klass.qualifiedName!!.substringBefore(".${klass.simpleName!!}"),
            simpleName = klass.simpleName!!,
            nullable = nullable
        )
    }
}
