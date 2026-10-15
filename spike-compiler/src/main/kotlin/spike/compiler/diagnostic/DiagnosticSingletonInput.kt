package spike.compiler.diagnostic

class DiagnosticSingletonInput(singleton: Involved, singletonName: String, input: String, factory: String) :
    Diagnostic {
    override val code = "SPIKE-SINGLETON-INPUT"
    override val summary = "@Singleton $singletonName depends on $input, a per-instance input of $factory"
    override val involved = listOf(singleton)
    override val reason = "Every entry point created by $factory shares its singletons, so $singletonName " +
        "would keep the $input of whichever instance created it first."
    override val fixes = listOf(
        Fix("Remove @Singleton from $singletonName, or stop it depending on $input.")
    )
}
