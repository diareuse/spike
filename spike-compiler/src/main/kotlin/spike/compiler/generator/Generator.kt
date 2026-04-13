package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec

interface Generator<Subject, Builder> {
    fun generate(chain: GeneratorChain<Subject, Builder>): Builder
}
