package spike.compiler.conversion

/**
 * Wraps a [ParameterConversion] with one parameter shape. Decorators apply in ascending `@Order`, the lowest
 * closest to the core.
 */
fun interface ParameterConversionDecorator {
    fun decorate(delegate: ParameterConversion): ParameterConversion
}

fun Map<Int, ParameterConversionDecorator>.applyTo(core: ParameterConversion): ParameterConversion =
    ordered().fold(core) { inner, decorator -> decorator.decorate(inner) }
