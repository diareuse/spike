package spike.compiler.diagnostic

class DiagnosticIncludeConstructorAmbiguous(
    name: String,
    constructors: List<Involved>,
    suggested: String,
) : Diagnostic {
    override val code = "SPIKE-INCLUDE-CONSTRUCTOR"
    override val summary = "@Include class $name has ${constructors.size} constructors, Spike cannot pick one"
    override val involved = constructors
    override val reason = "Spike calls exactly one constructor and never guesses which."
    override val fixes = listOf(
        Fix(
            "Mark the constructor Spike should call with @spike.Include.Constructor",
            "@spike.Include.Constructor\n$suggested"
        )
    )
}
