package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference
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
        val constructor = annotated.includedConstructor()
        context.builder.addConstructor(
            type = annotated.toType(false).qualifiedBy(annotated.findQualifiers()),
            invocation = constructor.toInvocation(),
            singleton = annotated.isAnnotationPresent(Singleton::class),
            origin = annotated.asOrigin(),
        )
    }

    override fun contribute(
        context: GraphContext,
        annotated: KSFunctionDeclaration,
    ) {
        val returnType = annotated.returnType!!.toQualifiedType(annotated)
        val parentType = annotated.parentDeclaration?.toType(false)?.qualifiedBy(annotated.findQualifiers())
        val method = Member.Method(
            packageName = annotated.packageName.asString(),
            name = annotated.simpleName.asString(),
            returns = returnType,
            parent = parentType,
        )
        val invocation = annotated.toInvocation()
        val receiver = annotated.receiver()
        context.builder.addFactory(
            type = returnType,
            member = method.on(receiver),
            invocation = invocation.copy(parameters = receiver.asParameters() + invocation.parameters),
            singleton = annotated.isAnnotationPresent(Singleton::class),
            origin = annotated.asOrigin(),
        )
    }

    override fun contribute(
        context: GraphContext,
        annotated: KSPropertyDeclaration,
    ) {
        val type = annotated.type.toQualifiedType(annotated)
        val property = Member.Property(
            packageName = annotated.packageName.asString(),
            name = annotated.simpleName.asString(),
            returns = type,
            parent = annotated.parentDeclaration?.toType(false),
        )
        val receiver = annotated.receiver()
        context.builder.addFactory(
            type = type,
            member = property.on(receiver),
            invocation = Invocation(receiver.asParameters(), singleton = false),
            singleton = annotated.isAnnotationPresent(Singleton::class),
            origin = annotated.asOrigin(),
        )
    }

    private fun KSFunctionDeclaration.receiver() = extensionReceiver?.let(::Receiver)

    private fun KSPropertyDeclaration.receiver() = extensionReceiver?.let(::Receiver)

    private fun Member.on(receiver: Receiver?): Member =
        if (receiver == null) this else Member.Receiver(receiver.type, this)

    private fun Receiver?.asParameters() =
        listOfNotNull(this?.let { Parameter("receiver", it.type, it.nullable) })

    private class Receiver(reference: KSTypeReference) {
        private val resolved = reference.resolve()
        val type = resolved.toType().qualifiedBy(reference.findQualifiers())
        val nullable = resolved.isMarkedNullable
    }
}
