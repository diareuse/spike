package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSAnnotated

class InjectionCheckConstructor(private val code: String, private val message: String) : ConversionCheck<KSAnnotated> {
    override fun check(subject: KSAnnotated) =
        if (Injection.of(subject) != null) null else ConversionProblem(code, message, subject)
}
