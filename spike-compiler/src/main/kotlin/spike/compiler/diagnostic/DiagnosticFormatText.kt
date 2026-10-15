package spike.compiler.diagnostic

class DiagnosticFormatText : DiagnosticFormat {
    override fun render(diagnostic: Diagnostic): String = buildString {
        append("[").append(diagnostic.code).append("] ").append(diagnostic.summary)
        for (involved in diagnostic.involved) {
            append("\n  - ").append(involved.description)
            involved.origin?.location?.let { append(" (").append(it).append(")") }
        }
        append("\nWhy: ").append(diagnostic.reason)
        for (fix in diagnostic.fixes) {
            append("\nFix: ").append(fix.description)
            fix.code?.let { code ->
                append(":")
                code.lines().forEach { append("\n    ").append(it) }
            }
        }
    }
}
