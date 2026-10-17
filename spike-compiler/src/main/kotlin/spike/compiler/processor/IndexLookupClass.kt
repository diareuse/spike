package spike.compiler.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSName

class IndexLookupClass : IndexLookup {
    override fun find(resolver: Resolver, name: KSName) = listOfNotNull(resolver.getClassDeclarationByName(name))
}
