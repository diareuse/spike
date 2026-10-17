package spike.compiler.processor

import spike.compiler.graph.GraphStore
import spike.Include

@Include
class ReplacementCheckAll(checks: Map<Int, ReplacementCheck>) : ReplacementCheck {
    private val checks = checks.toSortedMap().values
    override fun check(replacement: Replacement, production: GraphStore) {
        for (check in checks) check.check(replacement, production)
    }
}
