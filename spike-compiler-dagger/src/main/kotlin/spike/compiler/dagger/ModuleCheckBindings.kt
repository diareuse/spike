package spike.compiler.dagger

import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionProblem
import spike.Include
import spike.BindTarget
import spike.compiler.conversion.Order

@Include
class ModuleCheckBindings(private val check: ConversionCheck<DaggerBinding>) : ConversionCheck<DaggerModule> {
    override fun check(subject: DaggerModule): ConversionProblem? = subject.bindings.firstNotNullOfOrNull(check::check)

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(10)
        val ModuleCheckBindings.asCheck: ConversionCheck<DaggerModule>
            get() = this
    }
}
