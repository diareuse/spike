package spike.compiler.jsr

import com.google.devtools.ksp.getConstructors
import spike.compiler.conversion.AnnotationNames
import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionProblem
import spike.compiler.conversion.Injection

class InjectionCheckSingleConstructor(private val inject: AnnotationNames) : ConversionCheck<Injection> {
    override fun check(subject: Injection): ConversionProblem? {
        if (subject.owner.getConstructors().count(inject::annotates) < 2) return null
        return ConversionProblem(
            "SPIKE-JSR-CONSTRUCTORS",
            "Only one constructor of ${subject.owner.simpleName.asString()} may be annotated @Inject.",
            subject.owner,
        )
    }
}
