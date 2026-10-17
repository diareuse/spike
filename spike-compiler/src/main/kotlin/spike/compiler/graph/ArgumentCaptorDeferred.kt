package spike.compiler.graph

import spike.Include
import spike.BindTarget

@Include
class ArgumentCaptorDeferred : ArgumentCaptor {
    override fun captured(factory: TypeFactory, argument: TypeFactory.Argument): ArgumentCapture? {
        val deferred = factory as? TypeFactory.Deferred ?: return null
        val envelope = (deferred.type as? Type.Parametrized)?.envelope?.simpleName ?: deferred.type.simpleName
        return ArgumentCapture(
            captor = deferred.type.displayName,
            capturing = envelope,
            located = deferred.factory.type,
            fix = "Request ${deferred.factory.type.displayName} directly instead of through $envelope.",
        )
    }

    companion object {
        @Include(bindTo = BindTarget.Set)
        val ArgumentCaptorDeferred.asCaptor: ArgumentCaptor
            get() = this
    }
}
