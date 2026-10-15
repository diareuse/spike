package spike.compiler.diagnostic

class DiagnosticDuplicateMapKey(map: String, key: String, entries: List<Involved>) : Diagnostic {
    override val code = "SPIKE-DUPLICATE-MAP-KEY"
    override val summary = "$map has ${entries.size} entries with key $key, Spike cannot pick one"
    override val involved = entries
    override val reason = "Each key of a multibound Map maps to exactly one entry."
    override val fixes = listOf(Fix("Give each entry its own key, or remove one of them."))
}
