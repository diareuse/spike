package spike.compiler.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSName

/**
 * Finds the compiled declarations that an entry point index names under one prefix.
 */
fun interface IndexLookup {
    fun find(resolver: Resolver, name: KSName): List<KSAnnotated>
}
