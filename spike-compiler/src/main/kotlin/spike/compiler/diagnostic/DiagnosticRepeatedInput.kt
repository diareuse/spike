package spike.compiler.diagnostic

class DiagnosticRepeatedInput(subject: String, inputs: List<Involved>) : Diagnostic {
    override val code = "SPIKE-REPEATED-INPUT"
    override val summary = "$subject is an input of the entry point more than once, Spike cannot pick one"
    override val involved = inputs
    override val reason = "Spike wires each requested type to exactly one source and never picks one silently."
    override val fixes = listOf(
        Fix("Keep one input of type $subject, or stop requesting $subject."),
        Fix(
            "Or tell the inputs apart with a @spike.Qualifier-based annotation and request the qualified type",
            "@spike.Qualifier\nannotation class Named(val name: String)"
        ),
    )
}
