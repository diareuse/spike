package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration

class IncludeContributorChain(
    private vararg val contributors: IncludeContributor
) : IncludeContributor {
    override fun contribute(context: GraphContext, annotated: KSClassDeclaration) {
        contributors.forEach { it.contribute(context, annotated) }
    }

    override fun contribute(context: GraphContext, annotated: KSFunctionDeclaration) {
        contributors.forEach { it.contribute(context, annotated) }
    }
}