package spike.compiler.generator.container

import com.squareup.kotlinpoet.TypeSpec
import spike.compiler.generator.Generator
import spike.compiler.generator.TypeGenerator
import spike.compiler.generator.TypeGeneratorChain
import spike.compiler.generator.TypeResolver
import spike.compiler.graph.DependencyGraph
import spike.compiler.graph.GraphEntryPoint

class DependencyContainerTypeChain(
    override val subject: DependencyGraph,
    private val generators: List<TypeGenerator<DependencyGraph>>,
    override val resolver: TypeResolver,
    override val spec: TypeSpec.Builder = subject.entry.asTypeSpecBuilder(resolver),
) : TypeGeneratorChain<DependencyGraph> {
    fun proceed() = generators[0].generate(this)
    override fun proceed(origin: TypeGenerator<DependencyGraph>): TypeSpec.Builder {
        val index = generators.indexOf(origin) + 1
        if (index == generators.size) return spec
        return generators[index].generate(this)
    }

    private companion object {
        fun GraphEntryPoint.asTypeSpecBuilder(resolver: TypeResolver) = TypeSpec
            .classBuilder(resolver.getDependencyContainerClassName(type))
    }
}
