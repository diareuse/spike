package spike.compiler.kotlininject

import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionProblem

class ComponentCheckParent : ConversionCheck<KotlinInjectComponent> {
    override fun check(subject: KotlinInjectComponent): ConversionProblem? {
        if (subject.inputs.none(KotlinInjectNames.Component::annotates)) return null
        return ConversionProblem(
            "SPIKE-KI-PARENT",
            "Parent components are not converted; Spike has no component hierarchy, so merge the parent's bindings.",
            subject.declaration,
        )
    }
}
