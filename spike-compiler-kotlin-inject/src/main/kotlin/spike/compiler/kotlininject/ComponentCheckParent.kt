package spike.compiler.kotlininject

import spike.BindTarget
import spike.Include
import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.Order
import spike.compiler.conversion.ConversionProblem

@Include
class ComponentCheckParent : ConversionCheck<KotlinInjectComponent> {
    override fun check(subject: KotlinInjectComponent): ConversionProblem? {
        if (subject.inputs.none(KotlinInjectNames.Component::annotates)) return null
        return ConversionProblem(
            "SPIKE-KI-PARENT",
            "Parent components are not converted; Spike has no component hierarchy, so merge the parent's bindings.",
            subject.declaration,
        )
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(10)
        val ComponentCheckParent.asCheck: ConversionCheck<KotlinInjectComponent>
            get() = this
    }
}
