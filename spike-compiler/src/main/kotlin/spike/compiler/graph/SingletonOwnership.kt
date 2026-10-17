package spike.compiler.graph

sealed interface SingletonOwnership {
    fun inputCheck(bindings: GraphStore): SingletonInputCheck

    data object PerEntryPoint : SingletonOwnership {
        override fun inputCheck(bindings: GraphStore) = SingletonInputCheck { _, _ -> }
    }

    data object SharedByFactory : SingletonOwnership {
        override fun inputCheck(bindings: GraphStore) = SharedSingletonInputCheck(bindings)
    }
}

fun interface SingletonInputCheck {
    fun check(entry: GraphEntryPoint, roots: List<TypeFactory>)
}
