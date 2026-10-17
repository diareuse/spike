package spike.compiler.dagger

import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionProblem
import spike.Include
import spike.BindTarget
import spike.compiler.conversion.Order

@Include
class ComponentCheckBuilder : ConversionCheck<DaggerComponent> {
    override fun check(subject: DaggerComponent): ConversionProblem? {
        if (subject.nested.none(DaggerNames.ComponentBuilder::annotates)) return null
        return ConversionProblem(
            "SPIKE-DAGGER-BUILDER",
            "Spike converts @Component.Factory only; replace the @Component.Builder with a factory.",
            subject.declaration,
        )
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(10)
        val ComponentCheckBuilder.asCheck: ConversionCheck<DaggerComponent>
            get() = this
    }
}
