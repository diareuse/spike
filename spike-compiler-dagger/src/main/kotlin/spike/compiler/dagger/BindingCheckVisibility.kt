package spike.compiler.dagger

import com.google.devtools.ksp.isPrivate
import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionProblem
import spike.Include
import spike.BindTarget
import spike.compiler.conversion.Order

@Include
class BindingCheckVisibility : ConversionCheck<DaggerBinding> {
    override fun check(subject: DaggerBinding): ConversionProblem? {
        if (!subject.function.isPrivate()) return null
        return ConversionProblem(
            "SPIKE-DAGGER-VISIBILITY",
            "The generated @Include function cannot call a private @Provides; make it internal or public.",
            subject.function,
        )
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(10)
        val BindingCheckVisibility.asCheck: ConversionCheck<DaggerBinding>
            get() = this
    }
}
