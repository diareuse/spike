package spike.compiler.generator

import com.squareup.kotlinpoet.TypeSpec
import spike.graph.DependencyGraph
import spike.graph.GraphEntryPoint

data class DependencyContainerTypeChain(
    override val subject: DependencyGraph,
    private val generators: List<TypeGenerator<DependencyGraph>>,
    override val resolver: TypeResolver,
    override val spec: TypeSpec.Builder = subject.entry.asTypeSpecBuilder(),
    private val index: Int = 0
) : TypeGeneratorChain<DependencyGraph> {
    override fun proceed(): TypeSpec.Builder {
        if (index == generators.size) return spec
        return generators[index].generate(copy(index = index + 1))
    }

    private companion object {
        fun GraphEntryPoint.asTypeSpecBuilder() = TypeSpec.Companion
            .classBuilder(type.toClassName().peerClass { "Spike${it.simpleName}DependencyContainer" })
    }
}