package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import spike.Inject
import spike.Singleton
import spike.graph.Member

@OptIn(KspExperimental::class)
class IncludeContributorMain : IncludeContributor {
    override fun contribute(
        context: GraphContext,
        annotated: KSClassDeclaration
    ) {
        val constructors = annotated.getConstructors().toList()
        val constructor = when {
            constructors.size > 1 -> checkNotNull(constructors.firstOrNull { it.isAnnotationPresent(Inject::class) }) {
                "Include class (${annotated.qualifiedName?.asString()}) must have a constructor annotated with @spike.Inject if it has more than one constructor"
            }

            else -> constructors.single()
        }
        context.builder.addConstructor(
            type = annotated.toType().qualifiedBy(annotated.findQualifiers()),
            invocation = constructor.toInvocation(),
            singleton = annotated.isAnnotationPresent(Singleton::class)
        )
    }

    override fun contribute(
        context: GraphContext,
        annotated: KSFunctionDeclaration
    ) {
        val qualifiers = annotated.findQualifiers()
        val returnType = annotated.returnType!!.resolve().toType().qualifiedBy(qualifiers)
        val parentType = annotated.parentDeclaration?.toType()?.qualifiedBy(qualifiers)
        context.builder.addFactory(
            type = returnType,
            member = Member.Method(
                packageName = annotated.packageName.asString(),
                name = annotated.simpleName.asString(),
                returns = returnType,
                parent = parentType
            ),
            invocation = annotated.toInvocation(),
            singleton = annotated.isAnnotationPresent(Singleton::class)
        )
    }
}