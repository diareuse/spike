package spike.compiler.dagger

/**
 * Puts one way of reaching a Dagger binding in front of the others: it handles the binding or falls back. Applied
 * in ascending `@Order`, the lowest closest to [BindingCallStatic].
 */
fun interface BindingCallDecorator {
    fun decorate(fallback: BindingCall): BindingCall
}
