package spike.compiler.graph

/**
 * Recognises a factory that would keep a runtime argument beyond the call that passed it.
 */
fun interface ArgumentCaptor {
    fun captured(factory: TypeFactory, argument: TypeFactory.Argument): ArgumentCapture?
}

/**
 * How [captor] keeps an argument, described for the diagnostic, with the bindings of [located] involved.
 */
class ArgumentCapture(val captor: String, val capturing: String, val located: Type, val fix: String)
