package spike.compiler.diagnostic

fun interface DiagnosticFormat {
    fun render(diagnostic: Diagnostic): String
}
