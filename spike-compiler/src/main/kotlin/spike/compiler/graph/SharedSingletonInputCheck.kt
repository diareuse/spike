package spike.compiler.graph

import spike.compiler.diagnostic.DiagnosticException
import spike.compiler.diagnostic.DiagnosticSingletonInput
import spike.compiler.diagnostic.Involved
import java.util.IdentityHashMap

class SharedSingletonInputCheck(private val bindings: GraphStore = GraphStore()) {
    fun check(entry: GraphEntryPoint, roots: List<TypeFactory>) {
        if (!entry.factory.sharesSingletons) return
        var previous: Map<TypeFactory, TypeFactory.Property?> = emptyMap()
        do {
            val inputs = IdentityHashMap<TypeFactory, TypeFactory.Property?>()
            for (root in roots) inputReachedFrom(entry, root, inputs, previous)
            val cyclic = inputs.keys.any { it is TypeFactory.Recursion }
            val changed = inputs.values.count { it != null } != previous.values.count { it != null }
            previous = inputs
        } while (cyclic && changed)
    }

    private fun inputReachedFrom(
        entry: GraphEntryPoint,
        root: TypeFactory,
        inputs: MutableMap<TypeFactory, TypeFactory.Property?>,
        previous: Map<TypeFactory, TypeFactory.Property?>,
    ) {
        val pending = ArrayDeque<Pair<TypeFactory, Boolean>>()
        pending.addLast(root to false)
        while (pending.isNotEmpty()) {
            val (factory, expanded) = pending.removeLast()
            when {
                factory in inputs -> Unit
                expanded -> record(entry, factory, inputs, previous)
                else -> {
                    pending.addLast(factory to true)
                    for (child in factory.children().asReversed()) pending.addLast(child to false)
                }
            }
        }
    }

    private fun record(
        entry: GraphEntryPoint,
        factory: TypeFactory,
        inputs: MutableMap<TypeFactory, TypeFactory.Property?>,
        previous: Map<TypeFactory, TypeFactory.Property?>,
    ) {
        val input = factory as? TypeFactory.Property
            ?: (factory as? TypeFactory.Recursion)?.target?.let { previous[it] }
            ?: factory.children().firstNotNullOfOrNull { inputs[it] }
        inputs[factory] = input
        if (input != null && factory is TypeFactory.Callable && factory.singleton) {
            failOnSingletonInput(entry, factory, input)
        }
    }

    private fun TypeFactory.children(): List<TypeFactory> =
        if (this is TypeFactory.Deferred) dependencies + factory else dependencies

    private fun failOnSingletonInput(entry: GraphEntryPoint, singleton: TypeFactory, input: TypeFactory): Nothing {
        val name = singleton.type.displayName
        val involved = bindings.bindingsOf(singleton.type).firstOrNull()?.involved ?: Involved("@Singleton $name")
        throw DiagnosticException(
            DiagnosticSingletonInput(involved, name, input.type.displayName, entry.factory.type.displayName)
        )
    }
}
