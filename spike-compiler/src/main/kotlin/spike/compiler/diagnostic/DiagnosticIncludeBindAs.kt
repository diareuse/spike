package spike.compiler.diagnostic

class DiagnosticIncludeBindAs(declaration: String, fix: Fix, origin: Origin?) : Diagnostic {
    override val code = "SPIKE-INCLUDE-BIND-AS"
    override val summary = "bindAs was removed from @Include, used on $declaration"
    override val involved = listOf(Involved(declaration, origin))
    override val reason =
        "Every @Include property or function binds its declared type, so a binding is declared as one."
    override val fixes = listOf(fix)
}
