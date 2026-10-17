package spike.compiler.graph

import spike.compiler.diagnostic.DiagnosticArgumentCaptured
import spike.compiler.diagnostic.DiagnosticException
import java.util.IdentityHashMap

class ArgumentCaptureCheck(
    private val function: String,
    private val bindings: GraphStore,
    private val captors: List<ArgumentCaptor>,
) {
    fun check(root: TypeFactory) {
        var previous: Map<TypeFactory, TypeFactory.Argument?> = emptyMap()
        do {
            val reaches = IdentityHashMap<TypeFactory, TypeFactory.Argument?>()
            traverse(root, reaches, previous)
            val cyclic = reaches.keys.any { it is TypeFactory.Recursion }
            val changed = reaches.values.count { it != null } != previous.values.count { it != null }
            previous = reaches
        } while (cyclic && changed)
    }

    private fun traverse(
        root: TypeFactory,
        reaches: MutableMap<TypeFactory, TypeFactory.Argument?>,
        previous: Map<TypeFactory, TypeFactory.Argument?>,
    ) {
        val pending = ArrayDeque<Pair<TypeFactory, Boolean>>()
        pending.addLast(root to false)
        while (pending.isNotEmpty()) {
            val (factory, expanded) = pending.removeLast()
            when {
                factory in reaches -> Unit
                expanded -> record(factory, reaches, previous)
                else -> {
                    pending.addLast(factory to true)
                    for (child in factory.reached) pending.addLast(child to false)
                }
            }
        }
    }

    private fun record(
        factory: TypeFactory,
        reaches: MutableMap<TypeFactory, TypeFactory.Argument?>,
        previous: Map<TypeFactory, TypeFactory.Argument?>,
    ) {
        val argument = factory as? TypeFactory.Argument
            ?: (factory as? TypeFactory.Recursion)?.target?.let { previous[it] }
            ?: factory.reached.firstNotNullOfOrNull { reaches[it] }
        reaches[factory] = argument
        if (argument == null) return
        val capture = captors.firstNotNullOfOrNull { it.captured(factory, argument) } ?: return
        val involved = bindings.bindingsOf(capture.located).map { it.involved }
        throw DiagnosticException(
            DiagnosticArgumentCaptured(
                function,
                capture.captor,
                capture.capturing,
                argument.type.displayName,
                involved,
                capture.fix,
            )
        )
    }
}
