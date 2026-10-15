package spike.compiler.diagnostic

class DiagnosticIncludeNoConstructor(kind: String, name: String, origin: Origin?) : Diagnostic {
    override val code = "SPIKE-INCLUDE-CONSTRUCTOR"
    override val summary = "@Include $kind $name has no constructor, Spike cannot create it"
    override val involved = listOf(Involved("$kind $name", origin))
    override val reason = "Spike creates an @Include class by calling its constructor."
    override val fixes = listOf(
        Fix(
            "Put @Include on a class that implements $name, and bind it to $name with an @Include property",
            "@spike.Include\nclass ${name}Impl : $name\n@spike.Include\nval ${name}Impl.as$name: $name get() = this"
        )
    )
}
