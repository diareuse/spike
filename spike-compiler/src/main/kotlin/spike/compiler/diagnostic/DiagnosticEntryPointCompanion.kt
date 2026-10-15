package spike.compiler.diagnostic

class DiagnosticEntryPointCompanion(name: String, origin: Origin?) : Diagnostic {
    override val code = "SPIKE-ENTRY-POINT-COMPANION"
    override val summary = "@EntryPoint $name has no public or internal companion object"
    override val involved = listOf(Involved("interface $name", origin))
    override val reason = "Spike adds the entry point's creation functions to its companion object, such as $name()."
    override val fixes = listOf(
        Fix("Add a companion object to $name", "@spike.EntryPoint\ninterface $name {\n    companion object\n}")
    )
}
