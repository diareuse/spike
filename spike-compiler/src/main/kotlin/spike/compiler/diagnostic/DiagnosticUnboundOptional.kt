package spike.compiler.diagnostic

class DiagnosticUnboundOptional(type: String, nonNull: String, requiredBy: Involved?) : Diagnostic {
    override val code = "SPIKE-UNBOUND-OPTIONAL"
    override val summary = "$type has no binding, so Spike passes null"
    override val involved = listOfNotNull(requiredBy)
    override val reason = "A nullable dependency resolves to its own binding, then to a binding of $nonNull, " +
        "and to null only when neither exists."
    override val fixes = listOf(
        Fix("Add an @Include for $nonNull if a value was expected, or ignore this warning if null is intended."),
    )
}
