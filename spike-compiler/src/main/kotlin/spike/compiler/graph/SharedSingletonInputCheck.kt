package spike.compiler.graph

import spike.compiler.diagnostic.DiagnosticException
import spike.compiler.diagnostic.DiagnosticSingletonInput
import spike.compiler.diagnostic.Involved
import java.util.IdentityHashMap

class SharedSingletonInputCheck(private val bindings: GraphStore = GraphStore()) {
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
            when {
                factory in inputs -> Unit
                expanded -> record(entry, factory, inputs)
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
    ) {
        val input = factory as? TypeFactory.Property ?: factory.children().firstNotNullOfOrNull { inputs[it] }
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
