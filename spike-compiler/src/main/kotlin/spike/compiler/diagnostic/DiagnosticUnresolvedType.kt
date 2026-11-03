package spike.compiler.diagnostic

class DiagnosticUnresolvedType(type: String, usedBy: Involved) : Diagnostic {
    override val code = "SPIKE-UNRESOLVED-TYPE"
    override val summary = "$type cannot be resolved, but ${usedBy.description} needs it"
    override val involved = listOf(usedBy)
    override val reason = "Spike waits for types other processors generate, but $type was still unresolved " +
        "when processing finished. A graph built from an unresolved type would be wired against a guess."
    override val fixes = listOf(
        Fix("Import $type or add the dependency that declares it to this module"),
        Fix("Or, when a processor generates $type, make sure it runs in the same KSP task as Spike."),
    )
}
