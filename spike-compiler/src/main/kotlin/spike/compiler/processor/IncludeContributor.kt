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
        fun default(
            core: IncludeContributorMain,
            decorators: Map<Int, IncludeContributorDecorator>,
        ): IncludeContributor = decorators.toSortedMap().values.fold(core as IncludeContributor) { inner, decorator ->
            decorator.decorate(inner)
        }
    }
}
