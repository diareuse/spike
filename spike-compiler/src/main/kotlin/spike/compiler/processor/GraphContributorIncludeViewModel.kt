package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration

@OptIn(KspExperimental::class)
class GraphContributorIncludeViewModel(
    private val contributor: IncludeContributor
) : GraphContributor {
    override fun contribute(context: GraphContext, resolver: Resolver) {
        val classes = resolver
            .getSymbolsWithAnnotation("spike.lifecycle.viewmodel.SpikeViewModel")
            .filterIsInstance<KSClassDeclaration>()
        for (cls in classes) {
            contributor.contribute(context, cls)
        }
    }
}