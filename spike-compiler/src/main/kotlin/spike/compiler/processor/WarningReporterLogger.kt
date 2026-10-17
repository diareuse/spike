package spike.compiler.processor

import com.google.devtools.ksp.processing.KSPLogger
import spike.compiler.diagnostic.Diagnostic
import spike.compiler.diagnostic.DiagnosticFormat

class WarningReporterLogger(
    private val logger: KSPLogger,
    private val format: DiagnosticFormat,
) : WarningReporter {
    override fun warn(diagnostic: Diagnostic) {
        val node = diagnostic.involved.firstNotNullOfOrNull { (it.origin as? OriginNode)?.node }
        logger.warn(format.render(diagnostic), node)
    }
}
