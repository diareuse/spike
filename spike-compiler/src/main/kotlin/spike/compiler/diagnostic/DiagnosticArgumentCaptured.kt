package spike.compiler.diagnostic

class DiagnosticArgumentCaptured(
    function: String,
    captor: String,
    kind: String,
    argument: String,
    override val involved: List<Involved>,
    fix: String,
) : Diagnostic {
    override val code = "SPIKE-ARGUMENT-CAPTURED"
    override val summary = "$captor depends on $argument, an argument of $function"
    override val reason = "Each call of $function passes its own $argument, but a $kind outlives the call " +
        "and would keep the argument of whichever call created it first."
    override val fixes = listOf(Fix(fix))
}
