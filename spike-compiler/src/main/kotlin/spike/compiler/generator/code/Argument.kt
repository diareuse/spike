package spike.compiler.generator.code

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.FileGeneratorContext
import spike.compiler.generator.LinearReferences
import spike.compiler.generator.LinearStep
import spike.compiler.graph.Type

context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
fun CodeBlock.Builder.addArgument(index: Int, type: Type): CodeBlock.Builder =
    add(references.reference(step.arguments[index], context.resolver.getTypeName(type)))

context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
fun CodeBlock.Builder.addCastArgument(index: Int, type: Type): CodeBlock.Builder =
    add(references.castReference(step.arguments[index], context.resolver.getTypeName(type)))
