package spike.compiler.processor

import spike.compiler.graph.GraphStore

class ReplacementCheckAll(private val checks: List<ReplacementCheck>) : ReplacementCheck {
    override fun check(replacement: Replacement, production: GraphStore) {
        for (check in checks) check.check(replacement, production)
    }
}
