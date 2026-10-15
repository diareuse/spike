package spike.compiler.diagnostic

class DiagnosticEntryPointFactory(
    factory: String,
    factoryKind: String,
    factoryOrigin: Origin?,
    functions: List<Involved>,
    entryPoint: String,
    suggested: String,
) : Diagnostic {
    override val code = "SPIKE-ENTRY-POINT-FACTORY"
    override val summary =
        "@EntryPoint.Factory $factory declares ${functions.size} functions, Spike needs exactly one"
    override val involved = listOf(Involved("$factoryKind $factory", factoryOrigin)) + functions
    override val reason = "The factory's single function lists the entry point's inputs."
    override val fixes = listOf(
        Fix(
            "Keep one function that takes every input and returns $entryPoint; " +
                "pass null for an optional input declared nullable",
            suggested
        )
    )
}
