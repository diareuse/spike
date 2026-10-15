package spike.compiler.diagnostic

class DiagnosticMissingBinding(subject: String, simpleName: String, requiredBy: Involved) : Diagnostic {
    override val code = "SPIKE-MISSING-BINDING"
    override val summary = "$subject has no binding, but ${requiredBy.description} needs it"
    override val involved = listOf(requiredBy)
    override val reason = "Spike creates only types it has a binding for: " +
        "an @Include class, function or property, or an entry-point input."
    override val fixes = listOf(
        Fix(
            "Add an @Include binding for $subject",
            "@spike.Include\nfun ${simpleName.replaceFirstChar { it.lowercase() }}(): $simpleName = ..."
        ),
        Fix("Or pass $subject in through the entry point's @EntryPoint.Factory function."),
    )
}
