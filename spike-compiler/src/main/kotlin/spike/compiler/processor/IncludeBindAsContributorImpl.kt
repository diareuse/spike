package spike.compiler.processor

import com.google.devtools.ksp.symbol.*
import spike.Include
import spike.graph.AnyType
import kotlin.reflect.KProperty1

class IncludeBindAsContributorImpl : IncludeBindAsContributor {
    override fun contribute(context: GraphContext, annotated: KSClassDeclaration) {
        val bindAs: KSType = annotated.getAnnotationParameter(Include::bindAs)
        if (bindAs.toType() == AnyType) return
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
        if (bindAs.toType() == AnyType) return
        check(bindAs.isAssignableFrom(annotated.returnType!!.resolve())) {
            errorMessage(annotated, bindAs)
        }
        val qualifiers = annotated.findQualifiers()
        val targetType = bindAs.toType().qualifiedBy(qualifiers)
        val sourceType = annotated.returnType!!.toType().qualifiedBy(qualifiers)
        context.builder.addBinder(from = sourceType, to = targetType)
    }

    // ---

    private inline fun <reified A, reified P> KSAnnotated.getAnnotationParameter(parameter: KProperty1<A, Any?>): P {
        val annotation = annotations.single {
            it.annotationType.resolve().declaration.qualifiedName?.asString() == A::class.qualifiedName
        }
        return annotation.arguments.single { it.name?.asString() == parameter.name }.value as P
    }

    // ---

    private fun errorMessage(declaration: KSDeclaration, bindAs: KSType): String {
        return "Bind target (${declaration.qualifiedName?.asString()}) must be assignable from the '${Include::bindAs.name}' class (${bindAs.declaration.qualifiedName?.asString()})"
    }
}