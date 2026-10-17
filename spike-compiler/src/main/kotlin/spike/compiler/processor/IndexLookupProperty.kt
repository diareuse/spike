package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSName

@OptIn(KspExperimental::class)
class IndexLookupProperty : IndexLookup {
    override fun find(resolver: Resolver, name: KSName) =
        listOfNotNull(resolver.getPropertyDeclarationByName(name, true))
}
