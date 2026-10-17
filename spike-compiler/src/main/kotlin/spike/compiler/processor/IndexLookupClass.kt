package spike.compiler.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSName
import spike.BindTarget
import spike.Include

@Include
class IndexLookupClass : IndexLookup {
    override fun find(resolver: Resolver, name: KSName) = listOfNotNull(resolver.getClassDeclarationByName(name))

    companion object {
        @Include(bindTo = BindTarget.Map)
        @IndexPrefix(EntryPointIndex.CLASS)
        val IndexLookupClass.asIndexLookup: IndexLookup
            get() = this
    }
}
