package spike.compiler.processor

import spike.compiler.graph.GraphStore
import spike.Include

/**
 * Rejects a replacement that cannot take the place it claims in the production graph, by throwing its diagnostic.
 */
fun interface ReplacementCheck {
    fun check(replacement: Replacement, production: GraphStore)

    companion object {
        @Include
        val ReplacementCheckAll.asReplacementCheck: ReplacementCheck
            get() = this
    }
}
