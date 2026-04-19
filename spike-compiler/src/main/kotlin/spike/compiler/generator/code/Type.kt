package spike.compiler.generator.code

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.FileGeneratorContext
import spike.compiler.graph.Type

context(context: FileGeneratorContext)
inline fun CodeBlock.Builder.addType(type: Type, body: CodeBlock.Builder.() -> Unit) = apply {
    add("%T(", context.resolver.getTypeName(type))
    body()
    add(")")
}
