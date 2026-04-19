package spike.compiler.generator.code

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.FileGeneratorContext
import spike.compiler.graph.BuiltInMembers

context(context: FileGeneratorContext)
inline fun CodeBlock.Builder.addLazy(body: CodeBlock.Builder.() -> Unit) = apply {
    add("%M { ", context.resolver.builtInMember { BuiltInMembers.lazy })
    body()
    add(" }")
}
