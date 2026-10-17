package spike.compiler.conversion

import com.google.devtools.ksp.processing.KSPLogger

class ProblemReporterLogger(private val logger: KSPLogger) : ProblemReporter {
    override fun report(problem: ConversionProblem) {
        logger.error("[${problem.code}] ${problem.message}", problem.node)
    }
}
