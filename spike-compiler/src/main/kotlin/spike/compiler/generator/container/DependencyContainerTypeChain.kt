package spike.compiler.generator.container

import com.squareup.kotlinpoet.TypeSpec
import spike.compiler.generator.TypeGenerator
import spike.compiler.generator.TypeGeneratorChain
import spike.compiler.generator.TypeResolver
import spike.compiler.graph.DependencyGraph
import spike.compiler.graph.GraphEntryPoint

data class DependencyContainerTypeChain(
    override val subject: DependencyGraph,
    private val generators: List<TypeGenerator<DependencyGraph>>,
    override val resolver: TypeResolver,
    override val spec: TypeSpec.Builder = subject.entry.asTypeSpecBuilder(resolver),
    private val index: Int = 0
) : TypeGeneratorChain<DependencyGraph> {
    override fun proceed(): TypeSpec.Builder {
        if (index == generators.size) return spec
        return generators[index].generate(copy(index = index + 1))
    }

    private companion object {
        fun GraphEntryPoint.asTypeSpecBuilder(resolver: TypeResolver) = TypeSpec
            .classBuilder(resolver.getDependencyContainerClassName(type))
    }
}