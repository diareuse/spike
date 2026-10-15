package spike.compiler.processor

import com.google.devtools.ksp.processing.KSPLogger
import spike.compiler.diagnostic.Diagnostic
import spike.compiler.diagnostic.DiagnosticFormat

class DiagnosticReporterLogger(
    private val logger: KSPLogger,
    private val format: DiagnosticFormat,
) : DiagnosticReporter {
    override fun report(diagnostic: Diagnostic) {
        val node = diagnostic.involved.firstNotNullOfOrNull { (it.origin as? OriginNode)?.node }
        logger.error(format.render(diagnostic), node)
    }
}
