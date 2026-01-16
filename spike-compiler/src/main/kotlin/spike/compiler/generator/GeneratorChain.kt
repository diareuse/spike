package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec

typealias FileGeneratorChain<T> = GeneratorChain<T, FileSpec.Builder>
typealias TypeGeneratorChain<T> = GeneratorChain<T, TypeSpec.Builder>
typealias CodeBlockGeneratorChain<T> = GeneratorChain<T, CodeBlock.Builder>

interface GeneratorChain<Subject, Builder> {
    val spec: Builder
    val resolver: TypeResolver
    val subject: Subject
    fun proceed(): Builder
}