package spike.compiler.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration

/**
 * Reads the declarations that a compiled entry point's index lists, each found by the lookup of its prefix.
 */
class IndexedDeclarations(private val lookups: Map<String, IndexLookup>) {
    fun of(resolver: Resolver, entryPoint: KSClassDeclaration): List<KSAnnotated> {
        val name = EntryPointIndex.classNameOf(entryPoint.qualifiedName!!.asString()).canonicalName
        val index = resolver.getClassDeclarationByName(resolver.getKSNameFromString(name)) ?: return emptyList()
        val names = index.annotations
            .first { it.shortName.asString() == "IncludedDeclarations" }
            .arguments.single().value as List<*>
        return names.filterIsInstance<String>().flatMap { indexed ->
            val prefix = indexed.substringBefore(':') + ":"
            val lookup = lookups[prefix] ?: return@flatMap emptyList()
            lookup.find(resolver, resolver.getKSNameFromString(indexed.substringAfter(':')))
        }
    }
}
