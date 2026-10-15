package spike.compiler.diagnostic

class DiagnosticMapKeyType(
    map: String,
    value: String,
    requestedKey: String,
    requestedKeySimpleName: String,
    keys: List<String>,
    entries: List<Involved>,
) : Diagnostic {
    override val code = "SPIKE-MAP-KEY-TYPE"
    override val summary = "$map is requested, but every $value entry is keyed by ${keys.joinToString()}"
    override val involved = entries
    override val reason =
        "A multibound Map is built from the entries whose key annotation's parameter has the Map's key type."
    override val fixes = listOf(
        Fix("Request a Map keyed by ${keys.joinToString(" or ")} instead."),
        Fix(
            "Or key the entries with an annotation whose parameter is $requestedKey",
            "@spike.Key\nannotation class ${requestedKeySimpleName}Key(val value: $requestedKeySimpleName)"
        ),
    )
}
