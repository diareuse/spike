package spike.compiler.dagger

import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionProblem

class ModuleCheckBindings(private val check: ConversionCheck<DaggerBinding>) : ConversionCheck<DaggerModule> {
    override fun check(subject: DaggerModule): ConversionProblem? = subject.bindings.firstNotNullOfOrNull(check::check)
}
