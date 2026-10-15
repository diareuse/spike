package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import spike.Include
import spike.Singleton
import spike.compiler.graph.Invocation
import spike.compiler.graph.Member
import spike.compiler.graph.Parameter

@OptIn(KspExperimental::class)
class IncludeContributorMain : IncludeContributor {
    override fun contribute(
        context: GraphContext,
        annotated: KSClassDeclaration,
    ) {
        val constructors = annotated.getConstructors().toList()
        val constructor = when {
            constructors.size > 1 -> checkNotNull(constructors.firstOrNull { it.isAnnotationPresent(Include.Constructor::class) }) {
                "Include class (${annotated.qualifiedName?.asString()}) must have a constructor " +
                        "annotated with @spike.Include.Constructor if it has more than one constructor"
            }
            else -> constructors.singleOrNull()
                ?: error("Wanted exactly one constructor, but got ${constructors.size} in $annotated")
        }
        context.builder.addConstructor(
            type = annotated.toType(false).qualifiedBy(annotated.findQualifiers()),
            invocation = constructor.toInvocation(),
            singleton = annotated.isAnnotationPresent(Singleton::class),
        )
    }

    override fun contribute(
        context: GraphContext,
        annotated: KSFunctionDeclaration,
    ) {
        val qualifiers = annotated.findQualifiers()
        val returnType = annotated.returnType!!.resolve().toType().qualifiedBy(qualifiers)
        val parentType = annotated.parentDeclaration?.toType(false)?.qualifiedBy(qualifiers)
        val method = Member.Method(
            packageName = annotated.packageName.asString(),
            name = annotated.simpleName.asString(),
            returns = returnType,
            parent = parentType,
        )
        val invocation = annotated.toInvocation()
        val receiver = annotated.extensionReceiver?.resolve()
        context.builder.addFactory(
            type = returnType,
            member = method.on(receiver),
            invocation = invocation.copy(parameters = receiver.asParameters() + invocation.parameters),
            singleton = annotated.isAnnotationPresent(Singleton::class),
        )
    }

    override fun contribute(
        context: GraphContext,
        annotated: KSPropertyDeclaration,
    ) {
        val type = annotated.type.resolve().toType().qualifiedBy(annotated.findQualifiers())
        val property = Member.Property(
            packageName = annotated.packageName.asString(),
            name = annotated.simpleName.asString(),
            returns = type,
            parent = annotated.parentDeclaration?.toType(false),
        )
        val receiver = annotated.extensionReceiver?.resolve()
        context.builder.addFactory(
            type = type,
            member = property.on(receiver),
            invocation = Invocation(receiver.asParameters(), singleton = false),
            singleton = annotated.isAnnotationPresent(Singleton::class),
        )
    }

    private fun Member.on(receiver: KSType?): Member =
        if (receiver == null) this else Member.Receiver(receiver.toType(), this)

    private fun KSType?.asParameters() =
        listOfNotNull(this?.let { Parameter("receiver", it.toType(), it.isMarkedNullable) })
}
