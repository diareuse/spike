package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSName
import spike.Include
import spike.BindTarget

@OptIn(KspExperimental::class)
@Include
class IndexLookupFunction : IndexLookup {
    override fun find(resolver: Resolver, name: KSName) = resolver.getFunctionDeclarationsByName(name, true)
        .filter { it.isAnnotationPresent(Include::class) }
        .toList()

    companion object {
        @Include(bindTo = BindTarget.Map)
        @IndexPrefix(EntryPointIndex.FUNCTION)
        val IndexLookupFunction.asIndexLookup: IndexLookup
            get() = this
    }
}
