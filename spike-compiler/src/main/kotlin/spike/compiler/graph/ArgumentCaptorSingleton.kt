package spike.compiler.graph

import spike.Include
import spike.BindTarget

@Include
class ArgumentCaptorSingleton : ArgumentCaptor {
    override fun captured(factory: TypeFactory, argument: TypeFactory.Argument): ArgumentCapture? {
        val singleton = (factory as? TypeFactory.Callable)?.takeIf { it.singleton } ?: return null
        val name = singleton.type.displayName
        return ArgumentCapture(
            captor = "@Singleton $name",
            capturing = "@Singleton",
            located = singleton.type,
            fix = "Remove @Singleton from $name, or stop it depending on ${argument.type.displayName}.",
        )
    }

    companion object {
        @Include(bindTo = BindTarget.Set)
        val ArgumentCaptorSingleton.asCaptor: ArgumentCaptor
            get() = this
    }
}
