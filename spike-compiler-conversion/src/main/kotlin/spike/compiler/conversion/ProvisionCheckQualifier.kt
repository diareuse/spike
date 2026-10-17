package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSAnnotated

class ProvisionCheckQualifier(
    private val qualifiers: QualifierLookup,
    private val code: String,
    private val message: String,
) : ConversionCheck<Provision> {
    override fun check(subject: Provision): ConversionProblem? {
        val qualified = listOf<KSAnnotated>(subject.declaration) + subject.parameters
        if (qualified.none { qualifiers.unconvertible(it) != null }) return null
        return ConversionProblem(code, message, subject.declaration)
    }
}
