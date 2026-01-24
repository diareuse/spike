package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import spike.BindTarget
import spike.Include
import spike.compiler.processor.util.getAnnotationParameter

class IncludeContributorBindTo(
    private val origin: IncludeContributor
) : IncludeContributor {
    override fun contribute(
        context: GraphContext,
        annotated: KSClassDeclaration
    ) {
        val bindTo: KSClassDeclaration = annotated.getAnnotationParameter(Include::bindTo)
        val bindTarget = BindTarget.valueOf(bindTo.simpleName.asString())
        fun toType() = annotated.toType().qualifiedBy(annotated.findQualifiers())
        when (bindTarget) {
            BindTarget.None -> origin.contribute(context, annotated)
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
        annotated: KSFunctionDeclaration
    ) {
        val bindTo: KSClassDeclaration = annotated.getAnnotationParameter(Include::bindTo)
        val bindTarget = BindTarget.valueOf(bindTo.simpleName.asString())
        fun toType() = annotated.returnType!!.toType().qualifiedBy(annotated.findQualifiers())
        when (bindTarget) {
            BindTarget.None -> origin.contribute(context, annotated)
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
}
