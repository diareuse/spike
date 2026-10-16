package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.graph.TypeFactory

/**
 * Writes the expression that builds one variant of [TypeFactory] inside a program step.
 */
interface TypeFactoryCode<in T : TypeFactory> {
    context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
    fun write(body: CodeBlock.Builder, factory: T)
}
