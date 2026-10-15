package spike.compiler.diagnostic

class DiagnosticMapKeyMissing(declaration: String, name: String, signature: String, origin: Origin?) : Diagnostic {
    override val code = "SPIKE-MAP-KEY"
    override val summary = "@Include $declaration binds into a Map but has no key"
    override val involved = listOf(Involved(declaration, origin))
    override val reason = MAP_KEY_REASON
    override val fixes = listOf(
        Fix(
            "Declare a key annotation and put it on $name",
            "@spike.Key\nannotation class Named(val name: String)\n" +
                "@spike.Include(bindTo = spike.BindTarget.Map)\n@Named(\"$name\")\n$signature"
        )
    )
}

internal const val MAP_KEY_REASON = "Every Map entry needs a key, taken from one annotation marked with @spike.Key."
