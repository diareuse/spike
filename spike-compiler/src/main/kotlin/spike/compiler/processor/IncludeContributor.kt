package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import spike.Include

interface IncludeContributor {
    fun contribute(context: GraphContext, annotated: KSClassDeclaration)
    fun contribute(context: GraphContext, annotated: KSFunctionDeclaration)

    companion object {
        @Include
        fun default(): IncludeContributor {
            var out: IncludeContributor = IncludeContributorChain(
                IncludeContributorBindAs(),
                IncludeContributorMain(),
            )
            out = IncludeContributorBindTo(out)
            out = IncludeContributorMultiplatform(out)
            return out
        }
    }
}
