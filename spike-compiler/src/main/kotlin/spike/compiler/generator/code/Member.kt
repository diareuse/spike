package spike.compiler.generator.code

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.FileGeneratorContext
import spike.compiler.graph.Member

context(context: FileGeneratorContext)
inline fun CodeBlock.Builder.addMember(member: Member, body: CodeBlock.Builder.() -> Unit) = apply {
    add("%M(", context.resolver.getMemberName(member))
    body()
    add(")")
}
