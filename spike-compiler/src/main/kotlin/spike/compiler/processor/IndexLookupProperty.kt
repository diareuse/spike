package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSName
import spike.BindTarget
import spike.Include

@OptIn(KspExperimental::class)
@Include
class IndexLookupProperty : IndexLookup {
    override fun find(resolver: Resolver, name: KSName) =
        listOfNotNull(resolver.getPropertyDeclarationByName(name, true))

    companion object {
        @Include(bindTo = BindTarget.Map)
        @IndexPrefix(EntryPointIndex.PROPERTY)
        val IndexLookupProperty.asIndexLookup: IndexLookup
            get() = this
    }
}
