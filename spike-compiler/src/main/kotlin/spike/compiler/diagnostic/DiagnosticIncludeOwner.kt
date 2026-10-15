package spike.compiler.diagnostic

class DiagnosticIncludeOwner(
    kind: String,
    name: String,
    owner: String,
    origin: Origin?,
) : Diagnostic {
    override val code = "SPIKE-INCLUDE-OWNER"
    override val summary = "@Include $kind $name is declared in class $owner, Spike cannot call it"
    override val involved = listOf(Involved("$kind $name in class $owner", origin))
    override val reason = "Spike has no instance of $owner to call $name on."
    override val fixes = listOf(
        Fix("Declare $name at top level or inside an object, such as a companion object.")
    )
}
