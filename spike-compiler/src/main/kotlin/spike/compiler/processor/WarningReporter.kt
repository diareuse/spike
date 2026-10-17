package spike.compiler.processor

import spike.Include
import spike.compiler.diagnostic.Diagnostic

/**
 * Reports a diagnostic that does not fail the build.
 */
fun interface WarningReporter {
    fun warn(diagnostic: Diagnostic)

    companion object {
        @Include
        val WarningReporterLogger.reporter: WarningReporter
            get() = this
    }
}
