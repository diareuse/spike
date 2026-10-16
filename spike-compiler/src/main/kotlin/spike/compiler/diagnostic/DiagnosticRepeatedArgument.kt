package spike.compiler.diagnostic

class DiagnosticRepeatedArgument(function: String, argument: String, override val involved: List<Involved>) :
    Diagnostic {
    override val code = "SPIKE-REPEATED-ARGUMENT"
    override val summary = "$function takes $argument more than once, Spike cannot tell the arguments apart"
    override val reason = "Spike passes each argument of a function-type dependency to the parameter of its type, " +
        "so two arguments of one type would be ambiguous."
    override val fixes = listOf(
        Fix("Wrap the arguments in a class that holds both, and take that class as the single argument."),
    )
}
