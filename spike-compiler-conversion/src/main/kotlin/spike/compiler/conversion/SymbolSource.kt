package spike.compiler.conversion

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated

/**
 * Finds the declarations a converter converts in the sources of one processing round.
 */
fun interface SymbolSource {
    fun symbols(resolver: Resolver): List<KSAnnotated>
}
