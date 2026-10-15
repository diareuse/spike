package spike.compiler.diagnostic

interface Diagnostic {
    val code: String
    val summary: String
    val involved: List<Involved>
    val reason: String
    val fixes: List<Fix>
}
