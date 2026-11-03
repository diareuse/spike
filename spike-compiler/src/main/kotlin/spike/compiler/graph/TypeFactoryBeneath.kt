package spike.compiler.graph

val TypeFactory.beneath: List<TypeFactory>
    get() = when (this) {
        is TypeFactory.Assisted -> reached + factory
        is TypeFactory.Recursion -> listOfNotNull(target)
        else -> reached
    }
