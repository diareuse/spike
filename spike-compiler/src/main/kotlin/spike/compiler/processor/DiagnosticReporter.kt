package spike.compiler.processor

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import spike.Include
import spike.compiler.diagnostic.Diagnostic
import spike.compiler.diagnostic.DiagnosticFormatText

fun interface DiagnosticReporter {
    fun report(diagnostic: Diagnostic)

    companion object {
        @Include
        fun default(environment: SymbolProcessorEnvironment): DiagnosticReporter =
            DiagnosticReporterLogger(environment.logger, DiagnosticFormatText())
    }
}
