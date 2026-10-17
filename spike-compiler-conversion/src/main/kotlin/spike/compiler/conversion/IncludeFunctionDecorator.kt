package spike.compiler.conversion

/**
 * Wraps an [IncludeFunction] with one concern. Decorators apply in ascending `@Order`, the lowest closest to the core.
 */
fun interface IncludeFunctionDecorator<T : Declared> {
    fun decorate(delegate: IncludeFunction<T>): IncludeFunction<T>
}

fun <T : Declared> Map<Int, IncludeFunctionDecorator<T>>.applyTo(core: IncludeFunction<T>): IncludeFunction<T> =
    ordered().fold(core) { inner, decorator -> decorator.decorate(inner) }
