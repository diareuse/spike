package spike.compiler.generator.container

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import spike.compiler.generator.TypeGenerator
import spike.compiler.generator.TypeGeneratorChain
import spike.compiler.graph.DependencyGraph

class DependencyContainerTypeInternal : TypeGenerator<DependencyGraph> {
    override fun generate(chain: TypeGeneratorChain<DependencyGraph>): TypeSpec.Builder {
        chain.spec.addModifiers(KModifier.INTERNAL)
        return chain.proceed()
    }
}