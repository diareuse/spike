package spike.compiler.dagger

import com.google.devtools.ksp.getDeclaredFunctions
import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionProblem
import spike.compiler.conversion.QualifierLookup

class ComponentCheckQualifier(private val qualifiers: QualifierLookup) : ConversionCheck<DaggerComponent> {
    override fun check(subject: DaggerComponent): ConversionProblem? {
        val factory = subject.factory
        val parameters = factory?.getDeclaredFunctions()?.flatMap { it.parameters }.orEmpty()
        if (factory == null || parameters.none { qualifiers.unconvertible(it) != null }) return null
        return ConversionProblem(
            "SPIKE-DAGGER-QUALIFIER",
            "Only @Named is converted; annotate this qualifier with @spike.Qualifier instead.",
            factory,
        )
    }
}
