package spike.compiler.dagger

import com.google.devtools.ksp.symbol.KSAnnotated
import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionProblem
import spike.compiler.conversion.QualifierLookup

class BindingCheckQualifier(private val qualifiers: QualifierLookup) : ConversionCheck<DaggerBinding> {
    override fun check(subject: DaggerBinding): ConversionProblem? {
        val qualified = listOf<KSAnnotated>(subject.function) + subject.function.parameters
        if (qualified.none { qualifiers.unconvertible(it) != null }) return null
        return ConversionProblem(
            "SPIKE-DAGGER-QUALIFIER",
            "Only @Named is converted; annotate this qualifier with @spike.Qualifier instead.",
            subject.function,
        )
    }
}
