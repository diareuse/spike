package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.compiler.generator.requireKSFile
import spike.compiler.processor.symbol.SymbolRegistry

@OptIn(KspExperimental::class)
class GraphContributorIncludeViewModel(
    private val contributor: IncludeContributor,
    private val registry: SymbolRegistry
) : GraphContributor {
    override fun contribute(context: GraphContext) {
        for (cls in registry.spikeViewModel(context.resolver)) {
            contributor.contribute(context, cls)
            context.originatingFiles.add(cls.requireKSFile())
        }
    }
}
