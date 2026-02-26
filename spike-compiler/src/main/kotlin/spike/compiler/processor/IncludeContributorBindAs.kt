package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import spike.Include
import spike.compiler.processor.util.getAnnotationParameter
import spike.compiler.graph.BuiltInTypes

class IncludeContributorBindAs : IncludeContributor {
    override fun contribute(context: GraphContext, annotated: KSClassDeclaration) {
        val bindAs: KSType = annotated.getAnnotationParameter(Include::bindAs)
        if (bindAs.toType() == BuiltInTypes.Any) return
        check(bindAs.isAssignableFrom(annotated.asStarProjectedType())) {
            errorMessage(annotated, bindAs)
        }
        val qualifiers = annotated.findQualifiers()
        val targetType = bindAs.toType().qualifiedBy(qualifiers)
        val sourceType = annotated.toType().qualifiedBy(qualifiers)
        context.builder.addBinder(from = sourceType, to = targetType)
    }

    override fun contribute(context: GraphContext, annotated: KSFunctionDeclaration) {
        val bindAs: KSType = annotated.getAnnotationParameter(Include::bindAs)
        if (bindAs.toType() == BuiltInTypes.Any) return
        check(bindAs.isAssignableFrom(annotated.returnType!!.resolve())) {
            errorMessage(annotated, bindAs)
        }
        val qualifiers = annotated.findQualifiers()
        val targetType = bindAs.toType().qualifiedBy(qualifiers)
        val sourceType = annotated.returnType!!.resolve().toType().qualifiedBy(qualifiers)
        context.builder.addBinder(from = sourceType, to = targetType)
    }

    // ---

    private fun errorMessage(declaration: KSDeclaration, bindAs: KSType): String {
        return "Bind target (${declaration.qualifiedName?.asString()}) must be assignable from the '${Include::bindAs.name}' class (${bindAs.declaration.qualifiedName?.asString()})"
    }
}