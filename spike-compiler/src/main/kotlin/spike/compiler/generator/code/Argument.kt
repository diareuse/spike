package spike.compiler.generator.code

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.FileGeneratorContext
import spike.compiler.generator.LinearStep
import spike.compiler.graph.Type

context(context: FileGeneratorContext, step: LinearStep)
fun CodeBlock.Builder.addArgument(index: Int, type: Type) = add(
    "instances[%L] as %T",
    step.arguments[index],
    context.resolver.getTypeName(type)
)
