package spike.compiler.conversion

class ConversionCheckMapped<in T, R : Any>(
    private val map: (T) -> R?,
    private val check: ConversionCheck<R>,
) : ConversionCheck<T> {
    override fun check(subject: T): ConversionProblem? = map(subject)?.let(check::check)
}
