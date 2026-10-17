package spike.compiler.conversion

/**
 * Wraps an [EntryPointType] with one concern. Decorators apply in ascending `@Order`, the lowest closest to the core.
 */
fun interface EntryPointTypeDecorator<T : EntryPointSubject> {
    fun decorate(delegate: EntryPointType<T>): EntryPointType<T>
}

fun <T : EntryPointSubject> Map<Int, EntryPointTypeDecorator<T>>.applyTo(core: EntryPointType<T>): EntryPointType<T> =
    ordered().fold(core) { inner, decorator -> decorator.decorate(inner) }
