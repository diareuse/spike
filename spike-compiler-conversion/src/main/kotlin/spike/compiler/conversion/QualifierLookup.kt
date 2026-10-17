package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation

/**
 * Finds the qualifier annotation on a declaration that Spike cannot convert, if there is one.
 */
fun interface QualifierLookup {
    fun unconvertible(node: KSAnnotated): KSAnnotation?
}
