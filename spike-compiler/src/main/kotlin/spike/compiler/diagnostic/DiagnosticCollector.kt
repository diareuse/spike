package spike.compiler.diagnostic

class DiagnosticCollector {
    private val diagnostics = mutableListOf<Diagnostic>()

    fun attempt(block: () -> Unit) {
        try {
            block()
        } catch (e: DiagnosticException) {
            diagnostics += e.diagnostics
        }
    }

    fun throwCollected() {
        if (diagnostics.isNotEmpty()) throw DiagnosticException(diagnostics.toList())
    }
}
