package spike.compiler.conversion

class ConversionCheckFirst<in T>(private val checks: List<ConversionCheck<T>>) : ConversionCheck<T> {
    override fun check(subject: T): ConversionProblem? = checks.firstNotNullOfOrNull { it.check(subject) }
}
