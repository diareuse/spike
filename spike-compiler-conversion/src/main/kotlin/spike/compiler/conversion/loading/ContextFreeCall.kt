package spike.compiler.conversion.loading

import spike.Include
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandleProxies
import java.security.PrivilegedAction
import javax.security.auth.Subject

fun interface ContextFreeCall {
    fun call(target: MethodHandle): Any?

    companion object {
        @Include
        val ContextFreeCallSubject.call: ContextFreeCall
            get() = this
    }
}

/**
 * [Subject] is the caller of `doPrivileged` here, so the stack walk stops at a JDK frame. The action has to be a JDK
 * proxy: a class of ours would add its own domain above that frame.
 */
@Include
class ContextFreeCallSubject : ContextFreeCall {
    @Suppress("DEPRECATION")
    override fun call(target: MethodHandle): Any? =
        Subject.doAsPrivileged(null, MethodHandleProxies.asInterfaceInstance(PrivilegedAction::class.java, target), null)
}
