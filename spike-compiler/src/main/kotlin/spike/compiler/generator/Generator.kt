package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec

typealias FileGenerator<T> = Generator<T, FileSpec.Builder>
typealias TypeGenerator<T> = Generator<T, TypeSpec.Builder>
typealias CodeBlockGenerator<T> = Generator<T, CodeBlock.Builder>
interface Generator<Subject, Builder> {
    fun generate(chain: GeneratorChain<Subject, Builder>): Builder
}