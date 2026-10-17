package spike.compiler.graph

import spike.Include
import spike.compiler.diagnostic.Diagnostic

/**
 * Collects the warnings that resolving one graph produces.
 */
@Include
class GraphWarnings {
    private val collected = mutableListOf<Diagnostic>()

    val all: List<Diagnostic> get() = collected.toList()

    fun add(diagnostic: Diagnostic) {
        collected += diagnostic
    }
}
