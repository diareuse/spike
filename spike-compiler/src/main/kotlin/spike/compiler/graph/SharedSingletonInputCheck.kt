package spike.compiler.graph

import java.util.IdentityHashMap

class SharedSingletonInputCheck {
    fun check(entry: GraphEntryPoint, roots: List<TypeFactory>) {
        if (!entry.factory.sharesSingletons) return
        val inputs = IdentityHashMap<TypeFactory, TypeFactory.Property?>()
        for (root in roots) inputReachedFrom(entry, root, inputs)
    }

    private fun inputReachedFrom(
        entry: GraphEntryPoint,
        root: TypeFactory,
        inputs: MutableMap<TypeFactory, TypeFactory.Property?>,
    ) {
        val pending = ArrayDeque<Pair<TypeFactory, Boolean>>()
        pending.addLast(root to false)
        while (pending.isNotEmpty()) {
            val (factory, expanded) = pending.removeLast()
            if (factory in inputs) continue
            val children = factory.children()
            if (!expanded) {
                pending.addLast(factory to true)
                for (child in children.asReversed()) pending.addLast(child to false)
                continue
            }
            val input = factory as? TypeFactory.Property ?: children.firstNotNullOfOrNull { inputs[it] }
            inputs[factory] = input
            if (input != null && factory is TypeFactory.Callable && factory.singleton) {
                failOnSingletonInput(entry, factory, input)
            }
        }
    }

    private fun TypeFactory.children(): List<TypeFactory> =
        if (this is TypeFactory.Deferred) dependencies + factory else dependencies

    private fun failOnSingletonInput(entry: GraphEntryPoint, singleton: TypeFactory, input: TypeFactory): Nothing {
        val factory = entry.factory.type.displayName
        error(
            """@Singleton ${singleton.type.displayName} depends on ${input.type.displayName}, a per-instance input of $factory:
                |
                |<description>
                |  Every entry point created by $factory shares its singletons, so
                |  ${singleton.type.displayName} would keep the ${input.type.displayName} of whichever instance created it first.
                |  Remove @Singleton from ${singleton.type.displayName}, or stop it depending on ${input.type.displayName}.
                |</description>""".trimMargin()
        )
    }
}
