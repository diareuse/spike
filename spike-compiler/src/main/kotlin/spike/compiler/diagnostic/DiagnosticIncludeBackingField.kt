package spike.compiler.diagnostic

class DiagnosticIncludeBackingField(name: String, type: String, origin: Origin?) : Diagnostic {
    override val code = "SPIKE-INCLUDE-BACKING-FIELD"
    override val summary = "@Include property $name has a backing field, so every entry point would share one value"
    override val involved = listOf(Involved("property $name", origin))
    override val reason = "An entry point owns its values, but a backing field is created once for the whole program."
    override val fixes = listOf(
        Fix(
            "Compute the value in a getter, and add @spike.Singleton if one instance per entry point is wanted",
            "@spike.Include\nval $name: $type get() = ..."
        )
    )
}
