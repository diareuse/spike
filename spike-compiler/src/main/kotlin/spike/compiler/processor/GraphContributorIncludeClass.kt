package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.Include

@OptIn(KspExperimental::class)
class GraphContributorIncludeClass(
    private val contributor: IncludeContributor
) : GraphContributor {
    override fun contribute(context: GraphContext, resolver: Resolver) {
        val classes = resolver
            .getSymbolsWithAnnotation(Include::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
        for (cls in classes) {
            contributor.contribute(context, cls)
        }
    }
}
