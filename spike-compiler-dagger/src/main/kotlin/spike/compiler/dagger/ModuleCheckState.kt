package spike.compiler.dagger

import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionProblem

class ModuleCheckState : ConversionCheck<DaggerModule> {
    override fun check(subject: DaggerModule): ConversionProblem? {
        val needsInstance = subject.bindings.any { binding ->
            binding.isDeclaredInModule && !binding.isInObject && DaggerNames.Provides.annotates(binding.function)
        }
        val stateful = subject.declaration.primaryConstructor?.parameters?.isNotEmpty() == true
        if (!needsInstance || !stateful) return null
        return ConversionProblem(
            "SPIKE-DAGGER-MODULE-STATE",
            "Spike cannot construct a module with constructor parameters; move its @Provides into a companion object.",
            subject.declaration,
        )
    }
}
