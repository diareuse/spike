package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import spike.BindTarget
import spike.Include
import spike.compiler.graph.BuiltInTypes
import spike.compiler.graph.Type
import spike.compiler.processor.util.getAnnotationParameterOrDefault

class IncludeContributorBindTo(
    private val origin: IncludeContributor,
) : IncludeContributor {
    override fun contribute(
        context: GraphContext,
        annotated: KSClassDeclaration,
    ) {
        fun toType() = annotated.findBindAsOrSelf { this.toType() }
        origin.contribute(context, annotated)
        when (annotated.findBindToTarget()) {
            BindTarget.None -> {}
            BindTarget.Set -> context.multibind.addToSet(toType()) {
                origin.contribute(context.copy(builder = this), annotated)
            }
            BindTarget.List -> context.multibind.addToList(toType()) {
                origin.contribute(context.copy(builder = this), annotated)
            }
            BindTarget.Map -> context.multibind.addToMap(toType(), annotated.findKey()) {
                origin.contribute(context.copy(builder = this), annotated)
            }
        }
    }

    override fun contribute(
        context: GraphContext,
        annotated: KSFunctionDeclaration,
    ) {
        fun toType() = annotated.findBindAsOrSelf { returnType!!.resolve().toType() }
        origin.contribute(context, annotated)
        when (annotated.findBindToTarget()) {
            BindTarget.None -> {}
            BindTarget.Set -> context.multibind.addToSet(toType()) {
                origin.contribute(context.copy(builder = this), annotated)
            }
            BindTarget.List -> context.multibind.addToList(toType()) {
                origin.contribute(context.copy(builder = this), annotated)
            }
            BindTarget.Map -> context.multibind.addToMap(toType(), annotated.findKey()) {
                origin.contribute(context.copy(builder = this), annotated)
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
