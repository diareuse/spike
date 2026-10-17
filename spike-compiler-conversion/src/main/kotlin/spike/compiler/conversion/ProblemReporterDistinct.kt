package spike.compiler.conversion

class ProblemReporterDistinct(private val delegate: ProblemReporter) : ProblemReporter {
    private val reported = mutableSetOf<ConversionProblem>()

    override fun report(problem: ConversionProblem) {
        if (reported.add(problem)) delegate.report(problem)
    }
}
