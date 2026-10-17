package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSAnnotated

/**
 * Reads the string qualifier of a declaration, or null when it has none.
 */
fun interface NameLookup {
    fun name(node: KSAnnotated): String?
}
