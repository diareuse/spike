package spike.compiler.generator

/**
 * Wraps a [ContributionFunction] with one annotation concern, applied in ascending `@Order` around the core.
 */
fun interface ContributionFunctionDecorator {
    fun decorate(delegate: ContributionFunction): ContributionFunction
}
