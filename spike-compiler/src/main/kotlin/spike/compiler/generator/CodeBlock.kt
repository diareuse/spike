package spike.compiler.generator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.withIndent
import spike.compiler.graph.BuiltInMembers
import spike.compiler.graph.Invocation
import spike.compiler.graph.Member
import spike.compiler.graph.Parameter
import spike.compiler.graph.Type
import spike.compiler.graph.TypeFactory
import spike.factory.DependencyId

context(context: FileGeneratorContext)
fun CodeBlock.Builder.addParameter(index: Int, parameter: Parameter) = addBufferCast(index, parameter.type)

context(context: FileGeneratorContext)
fun CodeBlock.Builder.addParameters(invocation: Invocation) = apply {
    for ((index, parameter) in invocation.parameters.withIndex()) {
        if (index > 0) add(", ")
        addParameter(index, parameter)
    }
}

context(context: FileGeneratorContext)
fun CodeBlock.Builder.addParameters(factories: List<TypeFactory>) = apply {
    for ((index, parameter) in factories.withIndex()) {
        if (index > 0) add(", ")
        addBufferCast(index, parameter.type)
    }
}

context(context: FileGeneratorContext)
fun CodeBlock.Builder.addBufferCast(index: Int, type: Type) = add(
    "buffer[%L] as %T",
    index,
    context.resolver.getTypeName(type)
)

context(context: FileGeneratorContext)
inline fun CodeBlock.Builder.addLazy(body: CodeBlock.Builder.() -> Unit) = apply {
    add("%M { ", context.resolver.builtInMember { BuiltInMembers.lazy })
    body()
    add(" }")
}

context(context: FileGeneratorContext)
inline fun CodeBlock.Builder.addProvider(body: CodeBlock.Builder.() -> Unit) = apply {
    add("%T { ", context.resolver.builtInType { Provider })
    body()
    add(" }")
}

context(context: FileGeneratorContext)
fun CodeBlock.Builder.addDependencyFactoryCall(
    dependencyFactory: ClassName,
    factory: TypeFactory,
    type: Type = factory.type
) = add(
    "%T.get<%T>(%T(%L))",
    dependencyFactory,
    context.resolver.getTypeName(type),
    DependencyId::class,
    context.getDependencyId(factory)
)

context(context: FileGeneratorContext)
inline fun CodeBlock.Builder.addMember(member: Member, body: CodeBlock.Builder.() -> Unit) = apply {
    add("%M(", context.resolver.getMemberName(member))
    body()
    add(")")
}

context(context: FileGeneratorContext)
inline fun CodeBlock.Builder.addType(type: Type, body: CodeBlock.Builder.() -> Unit) = apply {
    add("%T(", context.resolver.getTypeName(type))
    body()
    add(")")
}

context(context: FileGeneratorContext)
inline fun CodeBlock.Builder.addMap(key: Type, value: Type, body: CodeBlock.Builder.() -> Unit) = apply {
    addStatement(
        "%M<%T, %T>(",
        context.resolver.builtInMember { mapOf },
        context.resolver.getTypeName(key),
        context.resolver.getTypeName(value)
    )
    withIndent {
        body()
    }
    add(")")
}