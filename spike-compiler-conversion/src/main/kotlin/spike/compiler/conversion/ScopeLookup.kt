package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSAnnotated

/**
 * Decides whether a declaration is scoped, which Spike treats as one instance per entry point.
 */
fun interface ScopeLookup {
    fun isScoped(node: KSAnnotated): Boolean
}
