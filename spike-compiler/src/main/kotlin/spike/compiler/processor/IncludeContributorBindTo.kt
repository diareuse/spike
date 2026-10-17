package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import spike.BindTarget
import spike.Include
import spike.compiler.graph.BuiltInTypes
import spike.compiler.graph.Type
import spike.compiler.processor.util.getAnnotationParameterOrDefault
import spike.compiler.Order

class IncludeContributorBindTo(
    private val origin: IncludeContributor,
) : IncludeContributor {
    override fun contribute(context: GraphContext, annotated: KSClassDeclaration) =
        annotated.contributeTo(context, { toType(false).qualifiedBy(findQualifiers()) }, origin::contribute)

    override fun contribute(context: GraphContext, annotated: KSFunctionDeclaration) =
        annotated.contributeTo(context, { returnType!!.toQualifiedType(this) }, origin::contribute)

    override fun contribute(context: GraphContext, annotated: KSPropertyDeclaration) =
        annotated.contributeTo(context, { type.toQualifiedType(this) }, origin::contribute)

    private inline fun <T : KSDeclaration> T.contributeTo(
        context: GraphContext,
        type: T.() -> Type,
        crossinline contribute: (GraphContext, T) -> Unit,
    ) {
        val annotated = this
        val bound = type()
        when (findBindToTarget()) {
            BindTarget.None -> contribute(context, annotated)
            BindTarget.Set -> context.multibind.addToSet(bound.elementOf(BuiltInTypes.Set)) {
                contribute(context.copy(builder = this), annotated)
            }
            BindTarget.List -> context.multibind.addToList(bound.elementOf(BuiltInTypes.List)) {
                contribute(context.copy(builder = this), annotated)
            }
            BindTarget.Map -> context.multibind.addToMap(bound, findKey()) {
                contribute(context.copy(builder = this), annotated)
            }
        }
    }

    private fun KSAnnotated.findBindToTarget(): BindTarget {
        val bindTo: KSClassDeclaration = getAnnotationParameterOrDefault(Include::bindTo, null)
            ?: return BindTarget.None
        return BindTarget.valueOf(bindTo.simpleName.asString())
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(40)
        fun decorator(): IncludeContributorDecorator = IncludeContributorDecorator(::IncludeContributorBindTo)
    }
}
