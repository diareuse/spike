package spike.compiler.diagnostic

class DiagnosticUnresolvedCompiled(type: String, compiled: String, consumers: List<Involved>) : Diagnostic {
    override val code = "SPIKE-UNRESOLVED-TYPE"
    override val summary = "$type cannot be resolved, but the compiled $compiled needs it"
    override val involved = listOf(Involved("compiled $compiled")) + consumers
    override val reason = "$type is not on this module's compile classpath, though a dependency's compiled binding " +
        "names it. A graph built from an unresolved type would be wired against a guess."
    override val fixes = listOf(
        Fix("Add the dependency that declares $type to this module."),
        Fix("Or expose it from the dependency that names it, with api instead of implementation."),
    )
}
