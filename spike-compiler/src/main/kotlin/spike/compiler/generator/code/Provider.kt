package spike.compiler.generator.code

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.FileGeneratorContext

context(context: FileGeneratorContext)
inline fun CodeBlock.Builder.addProvider(body: CodeBlock.Builder.() -> Unit) = apply {
    add("%T { ", context.resolver.builtInType { Provider })
    body()
    add(" }")
}
