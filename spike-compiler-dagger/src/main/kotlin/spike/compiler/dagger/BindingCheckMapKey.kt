package spike.compiler.dagger

import com.google.devtools.ksp.symbol.KSAnnotation
import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionProblem

class BindingCheckMapKey : ConversionCheck<DaggerBinding> {
    override fun check(subject: DaggerBinding): ConversionProblem? {
        val annotations = subject.function.annotations
        val keyless = DaggerNames.IntoMap.annotates(subject.function) &&
            annotations.none(DaggerNames.KnownKeys::matches)
        if (!keyless && annotations.none(::isCustomMapKey)) return null
        return ConversionProblem(
            "SPIKE-DAGGER-MAP-KEY",
            "An @IntoMap binding needs @StringKey, @IntKey, @LongKey or @ClassKey; other map keys are not converted.",
            subject.function,
        )
    }

    private fun isCustomMapKey(annotation: KSAnnotation) =
        DaggerNames.MapKey.annotatesAnnotationOf(annotation) && !DaggerNames.KnownKeys.matches(annotation)
}
