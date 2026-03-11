package spike.compiler.generator.container

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import spike.compiler.generator.FileGenerator
import spike.compiler.generator.FileGeneratorChain
import spike.compiler.generator.GeneratorChain
import spike.compiler.generator.GeneratorChainOrigin
import spike.compiler.generator.TypeGeneratorChain
import spike.compiler.graph.DependencyGraph

class DependencyContainerFileWithType(
    private val generator: GeneratorChainOrigin<DependencyGraph, TypeSpec.Builder>,
) : FileGenerator<DependencyGraph> {
    override fun generate(chain: FileGeneratorChain<DependencyGraph>): FileSpec.Builder {
        chain.spec.addType(generator.proceed().build())
        return chain.proceed(this)
    }
}
