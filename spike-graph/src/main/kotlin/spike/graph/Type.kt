package spike.graph

sealed class Type {
    data class Simple(val packageName: String, val simpleName: String) : Type() {
        override fun toString(): String {
            return "$packageName.$simpleName"
        }
    }
    data class Parametrized(val envelope: Type, val typeArguments: List<Type>) : Type() {
        override fun toString(): String {
            return "$envelope<${typeArguments.joinToString(", ")}>"
        }
    }
    data class Qualified(val qualifier: String, val type: Type) : Type() {
        override fun toString(): String {
            return "@$qualifier $type"
        }
    }
}