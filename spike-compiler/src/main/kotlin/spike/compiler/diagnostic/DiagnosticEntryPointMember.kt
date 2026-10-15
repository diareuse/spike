package spike.compiler.diagnostic

class DiagnosticEntryPointMember(
    entryPoint: String,
    name: String,
    returns: String,
    parameters: String,
    origin: Origin?,
) : Diagnostic {
    override val code = "SPIKE-ENTRY-POINT-MEMBER"
    override val summary = "Entry point function $entryPoint.$name takes parameters, " +
        "Spike passes inputs only through @EntryPoint.Factory"
    override val involved = listOf(Involved("function $name", origin))
    override val reason =
        "An entry point resolves its members from the graph, and the graph's inputs come from the factory."
    override val fixes = listOf(
        Fix(
            "Move the parameters to the factory function",
            """
            @spike.EntryPoint
            interface $entryPoint {
                val $name: $returns
                @spike.EntryPoint.Factory
                interface Factory {
                    fun create($parameters): $entryPoint
                }
                companion object
            }
            """.trimIndent()
        )
    )
}
