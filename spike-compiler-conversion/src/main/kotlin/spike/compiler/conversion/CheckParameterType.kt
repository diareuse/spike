package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSValueParameter

/**
 * Rejects a subject that takes a parameter of one of [types], which Spike cannot supply.
 */
class CheckParameterType<in T : Declared>(
    private val parameters: (T) -> List<KSValueParameter>,
    private val types: AnnotationNames,
    private val code: String,
    private val message: String,
) : ConversionCheck<T> {
    override fun check(subject: T): ConversionProblem? {
        if (parameters(subject).none { types.matches(it.type.resolve()) }) return null
        return ConversionProblem(code, message, subject.declaration)
    }
}
