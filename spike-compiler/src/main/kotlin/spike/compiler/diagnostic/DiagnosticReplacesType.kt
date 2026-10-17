package spike.compiler.diagnostic

class DiagnosticReplacesType(replaced: String, provided: String, replacement: Involved) : Diagnostic {
    override val code = "SPIKE-REPLACES-TYPE"
    override val summary = "@Replaces($replaced) is on a binding of $provided, which is not a $replaced"
    override val involved = listOf(replacement)
    override val reason = "Every consumer of $replaced receives the replacement, so it must be a $replaced."
    override val fixes = listOf(
        Fix("Make $provided implement or extend $replaced, or replace the type it does provide."),
    )
}
