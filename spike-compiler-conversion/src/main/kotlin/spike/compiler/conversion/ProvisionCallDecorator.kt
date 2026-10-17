package spike.compiler.conversion

/**
 * Puts one way of reaching a provision in front of the others: it handles the provision or falls back. Applied in
 * ascending `@Order`, the lowest closest to the core.
 */
fun interface ProvisionCallDecorator {
    fun decorate(fallback: ProvisionCall): ProvisionCall
}

fun Map<Int, ProvisionCallDecorator>.applyTo(core: ProvisionCall): ProvisionCall =
    ordered().fold(core) { inner, decorator -> decorator.decorate(inner) }
