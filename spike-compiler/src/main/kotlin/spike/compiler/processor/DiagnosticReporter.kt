package spike.compiler.processor

import spike.Include
import spike.compiler.diagnostic.Diagnostic

fun interface DiagnosticReporter {
    fun report(diagnostic: Diagnostic)

    companion object {
        @Include
        val DiagnosticReporterLogger.reporter: DiagnosticReporter
            get() = this
    }
}
