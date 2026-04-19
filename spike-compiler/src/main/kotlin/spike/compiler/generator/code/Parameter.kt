package spike.compiler.generator.code

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.FileGeneratorContext
import spike.compiler.graph.Invocation
import spike.compiler.graph.Parameter
import spike.compiler.graph.TypeFactory

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
