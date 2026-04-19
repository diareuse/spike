package spike.compiler.generator.code

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.FileGeneratorContext
import spike.compiler.graph.Type


context(context: FileGeneratorContext)
fun CodeBlock.Builder.addBufferCast(index: Int, type: Type) = add(
    "buffer[%L] as %T",
    index,
    context.resolver.getTypeName(type)
)
