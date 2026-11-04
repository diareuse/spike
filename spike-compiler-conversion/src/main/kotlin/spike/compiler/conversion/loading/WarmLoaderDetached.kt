package spike.compiler.conversion.loading

import spike.Include
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import java.net.URL
import java.net.URLClassLoader
import java.security.AccessControlContext
import java.security.AccessController
import java.security.ProtectionDomain

/**
 * Builds a loader that does not hold the context of the run creating it: `URLClassLoader` stores that context for its
 * whole life, and its domains would keep the per-run loader reachable.
 */
@Include
class WarmLoaderDetached(private val call: ContextFreeCall) {
    @Suppress("DEPRECATION")
    fun loader(classpath: ProcessorClasspath): URLClassLoader {
        val lookup = MethodHandles.publicLookup()
        val context = lookup.findStatic(
            AccessController::class.java,
            "getContext",
            MethodType.methodType(AccessControlContext::class.java),
        )
        check(call.call(context) == AccessControlContext(emptyArray<ProtectionDomain>())) {
            "A loader built here would keep the context of this run"
        }
        val constructor = lookup.findConstructor(
            URLClassLoader::class.java,
            MethodType.methodType(Void.TYPE, Array<URL>::class.java, ClassLoader::class.java),
        )
        val bound = MethodHandles.insertArguments(constructor, 0, classpath.urls.toTypedArray(), classpath.parent)
        return call.call(bound) as URLClassLoader
    }
}
