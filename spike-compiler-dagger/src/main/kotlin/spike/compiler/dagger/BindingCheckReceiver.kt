package spike.compiler.dagger

import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionProblem

class BindingCheckReceiver : ConversionCheck<DaggerBinding> {
    override fun check(subject: DaggerBinding): ConversionProblem? {
        if (subject.function.extensionReceiver == null) return null
        return ConversionProblem(
            "SPIKE-DAGGER-RECEIVER",
            "A @Provides extension function cannot be converted; take the receiver as a parameter instead.",
            subject.function,
        )
    }
}
