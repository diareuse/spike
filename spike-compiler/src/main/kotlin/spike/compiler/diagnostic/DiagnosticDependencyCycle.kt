package spike.compiler.diagnostic

class DiagnosticDependencyCycle(cycle: List<String>, bindings: List<Involved>) : Diagnostic {
    override val code = "SPIKE-DEPENDENCY-CYCLE"
    override val summary = "Circular dependency detected: ${cycle.joinToString(" -> ")}"
    override val involved = bindings
    override val reason =
        "Spike creates every dependency before the type that needs it, so a cycle has no type to create first."
    override val fixes = listOf(
        Fix("Remove one dependency of the cycle, or move what the types share into a new type that both depend on.")
    )
}
