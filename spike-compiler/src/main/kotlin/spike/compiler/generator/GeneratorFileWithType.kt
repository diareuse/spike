package spike.compiler.generator

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec

class GeneratorFileWithType<T>(
    private val type: GeneratorChainOrigin<T, TypeSpec.Builder>,
) : Generator<T, FileSpec.Builder> {
    override fun generate(chain: GeneratorChain<T, FileSpec.Builder>): FileSpec.Builder {
        chain.spec.addType(type.proceed().build())
        return chain.proceed(this)
    }
}