package spike.compiler.generator

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import spike.graph.DependencyGraph

class DependencyContainerTypeInternal : TypeGenerator<DependencyGraph> {
    override fun generate(chain: TypeGeneratorChain<DependencyGraph>): TypeSpec.Builder {
        chain.spec.addModifiers(KModifier.INTERNAL)
        return chain.proceed()
    }
}