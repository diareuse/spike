package spike.compiler.diagnostic

class DiagnosticException(val diagnostics: List<Diagnostic>) :
    RuntimeException(diagnostics.joinToString("\n") { it.summary }) {
    constructor(diagnostic: Diagnostic) : this(listOf(diagnostic))
}
