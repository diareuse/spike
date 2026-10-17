package spike.compiler.processor

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import spike.Include
import spike.compiler.diagnostic.Diagnostic
import spike.compiler.diagnostic.DiagnosticFormatText

/**
 * Reports a diagnostic that does not fail the build.
 */
fun interface WarningReporter {
    fun warn(diagnostic: Diagnostic)

    companion object {
        @Include
        fun default(environment: SymbolProcessorEnvironment): WarningReporter =
            WarningReporterLogger(environment.logger, DiagnosticFormatText())
    }
}
