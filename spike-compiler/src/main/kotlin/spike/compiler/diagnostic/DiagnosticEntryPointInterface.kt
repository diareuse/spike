package spike.compiler.diagnostic

class DiagnosticEntryPointInterface(name: String, kind: String, origin: Origin?) : Diagnostic {
    override val code = "SPIKE-ENTRY-POINT-INTERFACE"
    override val summary = "@EntryPoint $name is a $kind, Spike needs an interface"
    override val involved = listOf(Involved("$kind $name", origin))
    override val reason =
        "Spike generates the entry point's implementation, so the entry point only declares what it exposes."
    override val fixes = listOf(Fix("Declare $name as an interface", "@spike.EntryPoint\ninterface $name"))
}
