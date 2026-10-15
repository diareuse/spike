package spike.compiler.diagnostic

class DiagnosticMapKeyArity(
    annotation: String,
    declaration: String,
    parameters: Int,
    origin: Origin?,
    annotationOrigin: Origin?,
) : Diagnostic {
    override val code = "SPIKE-MAP-KEY"
    override val summary =
        "Key annotation $annotation on $declaration has $parameters parameters, Spike needs exactly one"
    override val involved = listOf(
        Involved(declaration, origin),
        Involved("annotation class $annotation", annotationOrigin),
    )
    override val reason = "The key annotation's single parameter is the Map key."
    override val fixes = listOf(
        Fix("Give $annotation exactly one parameter", "@spike.Key\nannotation class $annotation(val name: String)")
    )
}
