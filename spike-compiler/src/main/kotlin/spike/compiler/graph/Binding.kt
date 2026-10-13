package spike.compiler.graph

sealed interface Binding {
    val type: Type
    val description: String
}

internal val Type.displayName: String
    get() = toString().removePrefix(".")
