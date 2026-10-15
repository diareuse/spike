package spike.compiler.diagnostic

class DiagnosticMapKeyAmbiguous(declaration: String, name: String, keys: List<String>, origin: Origin?) : Diagnostic {
    override val code = "SPIKE-MAP-KEY"
    override val summary = "@Include $declaration binds into a Map but has ${keys.size} keys, Spike cannot pick one"
    override val involved = listOf(Involved(declaration, origin)) + keys.map { Involved("key $it") }
    override val reason = MAP_KEY_REASON
    override val fixes = listOf(Fix("Keep one key annotation on $name."))
}
