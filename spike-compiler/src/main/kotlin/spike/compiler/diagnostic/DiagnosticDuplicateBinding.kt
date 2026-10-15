package spike.compiler.diagnostic

class DiagnosticDuplicateBinding(subject: String, bindings: List<Involved>) : Diagnostic {
    override val code = "SPIKE-DUPLICATE-BINDING"
    override val summary = "Duplicate bindings for $subject, Spike cannot pick one"
    override val involved = bindings
    override val reason = "Spike wires each requested type to exactly one binding and never picks one silently."
    override val fixes = listOf(
        Fix("Keep exactly one binding for $subject."),
        Fix(
            "Or tell them apart with a @spike.Qualifier-based annotation and request the qualified type",
            "@spike.Qualifier\nannotation class Named(val name: String)"
        ),
    )
}
