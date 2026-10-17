package spike.compiler.processor

/**
 * Wraps an [IncludeContributor] with one concern. Decorators are applied in ascending `@Order`, the lowest
 * closest to the core.
 */
fun interface IncludeContributorDecorator {
    fun decorate(delegate: IncludeContributor): IncludeContributor
}
