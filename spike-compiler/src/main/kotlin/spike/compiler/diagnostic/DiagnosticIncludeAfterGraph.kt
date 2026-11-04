package spike.compiler.diagnostic

class DiagnosticIncludeAfterGraph(override val involved: List<Involved>) : Diagnostic {
    override val code = "SPIKE-INCLUDE-AFTER-GRAPH"
    override val summary = "${involved.first().description} was generated after Spike built the graph"
    override val reason = "Spike builds the graph in the first KSP round when every processor on the processor " +
        "classpath is known not to write. This declaration came from a processor Spike could not see, so the graph " +
        "does not include it."
    override val fixes = listOf(
        Fix("Register the processor in META-INF/services so that Spike finds it and waits for its output."),
    )
}
