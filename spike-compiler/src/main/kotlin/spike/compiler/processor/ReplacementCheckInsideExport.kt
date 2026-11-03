package spike.compiler.processor

import spike.Include
import spike.compiler.diagnostic.DiagnosticException
import spike.compiler.diagnostic.DiagnosticReplacesInsideExport

@Include
class ReplacementCheckInsideExport : ReplacementCheck {
    override fun check(replacement: Replacement, production: ProductionBindings) {
        val users = production.usersOf(replacement.replaced)
        if (users.isEmpty()) return
        throw DiagnosticException(
            DiagnosticReplacesInsideExport(replacement.replacedName, users.map { it.description }, replacement.involved)
        )
    }
}
