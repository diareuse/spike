package spike.compiler.conversion

/**
 * Rejects a subject whose declaration carries one of [names], which Spike has no counterpart for.
 */
class CheckAnnotated<in T : Declared>(
    private val names: AnnotationNames,
    private val code: String,
    private val message: String,
) : ConversionCheck<T> {
    override fun check(subject: T): ConversionProblem? {
        if (!names.annotates(subject.declaration)) return null
        return ConversionProblem(code, message, subject.declaration)
    }
}
