package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import spike.Include

interface IncludeContributor {
    fun contribute(context: GraphContext, annotated: KSClassDeclaration)
    fun contribute(context: GraphContext, annotated: KSFunctionDeclaration)
    fun contribute(context: GraphContext, annotated: KSPropertyDeclaration)

    companion object {
        @Include
        fun default(): IncludeContributor {
            var out: IncludeContributor = IncludeContributorMain()
            out = IncludeContributorOwner(out)
            out = IncludeContributorBindAsRemoved(out)
            out = IncludeContributorBackingField(out)
            out = IncludeContributorBindTo(out)
            out = IncludeContributorMultiplatform(out)
            return out
        }
    }
}
