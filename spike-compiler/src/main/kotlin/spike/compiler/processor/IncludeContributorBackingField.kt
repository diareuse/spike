package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import spike.compiler.diagnostic.DiagnosticException
import spike.compiler.diagnostic.DiagnosticIncludeBackingField
import spike.BindTarget
import spike.Include
import spike.compiler.Order

class IncludeContributorBackingField(
    private val origin: IncludeContributor,
) : IncludeContributor {
    override fun contribute(context: GraphContext, annotated: KSClassDeclaration) =
        origin.contribute(context, annotated)

    override fun contribute(context: GraphContext, annotated: KSFunctionDeclaration) =
        origin.contribute(context, annotated)

    override fun contribute(context: GraphContext, annotated: KSPropertyDeclaration) {
        if (annotated.hasBackingField) {
            val name = annotated.simpleName.asString()
            throw DiagnosticException(
                DiagnosticIncludeBackingField(name, annotated.type.resolve().rendered(), annotated.asOrigin())
            )
        }
        origin.contribute(context, annotated)
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(30)
        fun decorator(): IncludeContributorDecorator = IncludeContributorDecorator(::IncludeContributorBackingField)
    }
}
