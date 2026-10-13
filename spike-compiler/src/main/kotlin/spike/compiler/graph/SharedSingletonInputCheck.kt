package spike.compiler.graph

import spike.compiler.graph.TypeFactory.Companion.dependencyTree

class SharedSingletonInputCheck {
    fun check(entry: GraphEntryPoint, roots: List<TypeFactory>) {
        if (!entry.factory.sharesSingletons) return
        val singletons = roots.asSequence()
            .flatMap { it.dependencyTree() }
            .filterIsInstance<TypeFactory.Callable>()
            .filter { it.singleton }
            .distinct()
        for (singleton in singletons) {
            val input = singleton.dependencyTree().filterIsInstance<TypeFactory.Property>().firstOrNull() ?: continue
            error(
                """@Singleton ${singleton.type.displayName} depends on ${input.type.displayName}, a per-instance input of ${entry.factory.type.displayName}:
                    |
                    |<description>
                    |  Every entry point created by ${entry.factory.type.displayName} shares its singletons, so
                    |  ${singleton.type.displayName} would keep the ${input.type.displayName} of whichever instance created it first.
                    |  Remove @Singleton from ${singleton.type.displayName}, or stop it depending on ${input.type.displayName}.
                    |</description>""".trimMargin()
            )
        }
    }
}
