package spike.compiler.conversion

/**
 * Decides whether [T] can be converted to a Spike declaration, and returns the reason when it cannot.
 */
fun interface ConversionCheck<in T> {
    fun check(subject: T): ConversionProblem?
}
