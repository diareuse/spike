package spike.compiler.conversion.loading

import spike.Include
import java.security.AccessControlContext
import java.security.AccessController
import java.security.PrivilegedAction
import java.security.ProtectionDomain

fun interface CallerContexts {
    fun carried(): Boolean

    companion object {
        @Include
        val CallerContextsProbed.contexts: CallerContexts
            get() = this
    }
}

/**
 * A JDK that dropped the security manager returns one fixed context whatever is on the stack, so a context it stores
 * cannot hold a caller's domain. It shows by ignoring a domain handed to `doPrivileged`.
 */
@Include
class CallerContextsProbed : CallerContexts {
    @Suppress("DEPRECATION")
    override fun carried(): Boolean {
        val marked = AccessControlContext(arrayOf(ProtectionDomain(null, null)))
        val seen = AccessController.doPrivileged(PrivilegedAction { AccessController.getContext() }, marked)
        return seen != AccessController.getContext()
    }
}
