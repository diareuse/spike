package spike.compiler.conversion

import com.google.devtools.ksp.isPrivate

class InjectionCheckVisibility(private val code: String) : ConversionCheck<Injection> {
    override fun check(subject: Injection): ConversionProblem? {
        if (!subject.constructor.isPrivate()) return null
        return ConversionProblem(
            code,
            "The generated @Include function cannot call a private @Inject constructor; make it internal or public.",
            subject.constructor,
        )
    }
}
