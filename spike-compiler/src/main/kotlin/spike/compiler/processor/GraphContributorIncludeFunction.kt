package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import spike.Include
import spike.compiler.generator.requireKSFile

class GraphContributorIncludeFunction(
    private val bindAs: IncludeContributor,
) : GraphContributor {
    @OptIn(KspExperimental::class)
    override fun contribute(context: GraphContext, resolver: Resolver) {
        val functions = resolver
            .getSymbolsWithAnnotation(Include::class.qualifiedName!!)
            .filterIsInstance<KSFunctionDeclaration>()
        for (func in functions) {
            bindAs.contribute(context, func)
            context.originatingFiles.add(func.requireKSFile())
        }
    }
}
