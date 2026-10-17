package spike.compiler.diagnostic

class DiagnosticReplacesNothing(replaced: String, base: String, replacement: Involved) : Diagnostic {
    override val code = "SPIKE-REPLACES-NOTHING"
    override val summary = "@Replaces($replaced) has nothing to replace: $base has no binding of $replaced"
    override val involved = listOf(replacement)
    override val reason = "A replacement stands in for a production binding; without one it would silently add " +
        "a new binding instead."
    override val fixes = listOf(
        Fix("Replace a type that $base binds, or drop @Replaces and keep this as a plain @Include."),
    )
}
