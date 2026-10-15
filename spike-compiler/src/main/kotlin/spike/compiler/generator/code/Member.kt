package spike.compiler.generator.code

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.FileGeneratorContext
import spike.compiler.generator.LinearReferences
import spike.compiler.generator.LinearStep
import spike.compiler.graph.Invocation
import spike.compiler.graph.Member

context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
fun CodeBlock.Builder.addCall(member: Member, invocation: Invocation): CodeBlock.Builder = apply {
    val name = context.resolver.getMemberName(member)
    when (member) {
        is Member.Method -> add("%M(", name).addParameters(invocation).add(")")
        is Member.Property -> add("%M", name)
        is Member.Receiver -> {
            addParameter(0, invocation.parameters.first()).add(".%M", name)
            if (member.member is Member.Method) add("(").addParameters(invocation, from = 1).add(")")
        }
    }
}
