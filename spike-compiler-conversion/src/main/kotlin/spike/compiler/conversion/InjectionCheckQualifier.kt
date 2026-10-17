package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSAnnotated

class InjectionCheckQualifier(
    private val qualifiers: QualifierLookup,
    private val code: String,
    private val message: String,
) : ConversionCheck<Injection> {
    override fun check(subject: Injection): ConversionProblem? {
        val qualified = listOf<KSAnnotated>(subject.owner) + subject.constructor.parameters
        if (qualified.none { qualifiers.unconvertible(it) != null }) return null
        return ConversionProblem(code, message, subject.owner)
    }
}
