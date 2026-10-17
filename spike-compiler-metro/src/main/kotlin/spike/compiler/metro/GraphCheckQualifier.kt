package spike.compiler.metro

import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionProblem
import spike.compiler.conversion.QualifierLookup

class GraphCheckQualifier(
    private val qualifiers: QualifierLookup,
    private val code: String,
    private val message: String,
) : ConversionCheck<MetroGraph> {
    override fun check(subject: MetroGraph): ConversionProblem? {
        if (subject.inputs.none { qualifiers.unconvertible(it) != null }) return null
        return ConversionProblem(code, message, subject.declaration)
    }
}
