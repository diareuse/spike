package spike.compiler.diagnostic

class DiagnosticInputConflict(subject: String, inputs: List<Involved>, bindings: List<Involved>) : Diagnostic {
    override val code = "SPIKE-INPUT-CONFLICT"
    override val summary = "$subject is both an input of the entry point and an @Include binding, Spike cannot pick one"
    override val involved = inputs + bindings
    override val reason = "Spike wires each requested type to exactly one source and never picks one silently."
    override val fixes = listOf(
        Fix("Remove the @Include binding of $subject, or stop passing $subject in."),
        Fix(
            "Or tell them apart with a @spike.Qualifier-based annotation and request the qualified type",
            "@spike.Qualifier\nannotation class Named(val name: String)"
        ),
    )
}
