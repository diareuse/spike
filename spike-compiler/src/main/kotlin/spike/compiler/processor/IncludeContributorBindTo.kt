package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import spike.BindTarget
import spike.Include
import spike.compiler.graph.BuiltInTypes
import spike.compiler.graph.Type
import spike.compiler.processor.util.getAnnotationParameterOrDefault

class IncludeContributorBindTo(
    private val origin: IncludeContributor,
) : IncludeContributor {
    override fun contribute(context: GraphContext, annotated: KSClassDeclaration) =
        annotated.contributeTo(context, { toType(false) }, origin::contribute)

    override fun contribute(context: GraphContext, annotated: KSFunctionDeclaration) =
        annotated.contributeTo(context, { returnType!!.resolve().toType() }, origin::contribute)

    override fun contribute(context: GraphContext, annotated: KSPropertyDeclaration) =
        annotated.contributeTo(context, { type.resolve().toType() }, origin::contribute)

    private inline fun <T : KSAnnotated> T.contributeTo(
        context: GraphContext,
        type: T.() -> Type,
        crossinline contribute: (GraphContext, T) -> Unit,
    ) {
        val annotated = this
        when (findBindToTarget()) {
            BindTarget.None -> contribute(context, annotated)
            BindTarget.Set -> context.multibind.addToSet(findBindAsOrSelf(type)) {
                contribute(context.copy(builder = this), annotated)
            }
            BindTarget.List -> context.multibind.addToList(findBindAsOrSelf(type)) {
                contribute(context.copy(builder = this), annotated)
            }
            BindTarget.Map -> context.multibind.addToMap(findBindAsOrSelf(type), findKey()) {
                contribute(context.copy(builder = this), annotated)
            }
        }
    }

    private fun KSAnnotated.findBindToTarget(): BindTarget {
        val bindTo: KSClassDeclaration = getAnnotationParameterOrDefault(Include::bindTo, null)
            ?: return BindTarget.None
        return BindTarget.valueOf(bindTo.simpleName.asString())
    }

    private inline fun <T : KSAnnotated> T.findBindAsOrSelf(type: T.() -> Type): Type {
        val bindAs: KSType? = getAnnotationParameterOrDefault(Include::bindAs, null)
        val targetType =
            if (bindAs == null || bindAs.toType() == BuiltInTypes.Any) type()
            else bindAs.toType()
        return targetType.qualifiedBy(findQualifiers())
    }
}
