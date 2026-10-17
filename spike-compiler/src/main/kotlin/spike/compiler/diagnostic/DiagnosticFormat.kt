package spike.compiler.diagnostic

import spike.Include

fun interface DiagnosticFormat {
    fun render(diagnostic: Diagnostic): String

    companion object {
        @Include
        val DiagnosticFormatText.format: DiagnosticFormat
            get() = this
    }
}
