package spike.compiler.generator

import com.squareup.kotlinpoet.FileSpec
import spike.graph.DependencyGraph

class DependencyContainerFileWithType(
    private val generator: TypeGeneratorChain<DependencyGraph>
) : FileGenerator<DependencyGraph> {
    override fun generate(chain: FileGeneratorChain<DependencyGraph>): FileSpec.Builder {
        chain.spec.addType(generator.proceed().build())
        return chain.proceed()
    }
}