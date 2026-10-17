package spike.compiler.conversion

/**
 * Reports a declaration that cannot be converted. Reporting an error fails the build once processing finishes.
 */
fun interface ProblemReporter {
    fun report(problem: ConversionProblem)
}
