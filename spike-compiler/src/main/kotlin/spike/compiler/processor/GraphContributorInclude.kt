package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import spike.compiler.generator.requireKSFile
import spike.compiler.processor.symbol.SymbolRegistry

@OptIn(KspExperimental::class)
class GraphContributorInclude(
    private val contributor: IncludeContributor,
    private val registry: SymbolRegistry
) : GraphContributor {
    override fun contribute(context: GraphContext, resolver: Resolver) {
        for (ks in registry.include(resolver)) {
            when (ks) {
                is KSClassDeclaration -> contributor.contribute(context, ks)
                is KSFunctionDeclaration -> contributor.contribute(context, ks)
                else -> error(
                    """@spike.Include // <-- cannot be used on this element 
                    |$ks
                    |
                    |<description>
                    | Include must be used only on functions and classes. All other uses are prohibited.
                    |</description>
                """.trimMargin()
                )
            }
            context.originatingFiles.add(ks.requireKSFile())
        }
    }
}
