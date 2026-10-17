package spike.compiler.conversion

class InjectionCheckGeneric(private val code: String) : ConversionCheck<Injection> {
    override fun check(subject: Injection): ConversionProblem? {
        if (subject.owner.typeParameters.isEmpty()) return null
        return ConversionProblem(
            code,
            "Spike cannot convert @Inject constructors of generic classes; add an @Include function instead.",
            subject.owner,
        )
    }
}
