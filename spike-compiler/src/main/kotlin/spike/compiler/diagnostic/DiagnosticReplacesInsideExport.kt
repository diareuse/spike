package spike.compiler.diagnostic

class DiagnosticReplacesInsideExport(replaced: String, users: List<String>, replacement: Involved) : Diagnostic {
    override val code = "SPIKE-REPLACES-INSIDE-EXPORT"
    override val summary = "@Replaces($replaced) cannot reach inside a compiled @Export graph: " +
        "${users.joinToString()} would keep the original $replaced"
    override val involved = listOf(replacement) + users.map { Involved(it) }
    override val reason = "An @Export graph is compiled in its own module, so a replacement takes the place of " +
        "$replaced only where the consuming module builds it; the exported bindings above were built with the " +
        "original and the graph would not be wired as declared."
    override val fixes = listOf(
        Fix("Replace those exported bindings with @Replaces too, so the test graph builds them from the replacement."),
        Fix("Or move the bindings that use $replaced out of the @Export module into the module that consumes them."),
    )
}
