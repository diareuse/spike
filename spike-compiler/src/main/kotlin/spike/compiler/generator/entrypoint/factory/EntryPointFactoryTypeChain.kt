package spike.compiler.generator.entrypoint.factory

import com.squareup.kotlinpoet.TypeSpec
import spike.compiler.generator.TypeGenerator
import spike.compiler.generator.TypeGeneratorChain
import spike.compiler.generator.TypeResolver
import spike.graph.GraphEntryPoint

data class EntryPointFactoryTypeChain(
    override val subject: GraphEntryPoint.Factory,
    private val generators: List<TypeGenerator<GraphEntryPoint.Factory>>,
    override val resolver: TypeResolver,
    override val spec: TypeSpec.Builder = subject.asTypeSpecBuilder(resolver),
    private val index: Int = 0
) : TypeGeneratorChain<GraphEntryPoint.Factory> {
    override fun proceed(): TypeSpec.Builder {
        if (index == generators.size) return spec
        return generators[index].generate(copy(index = index + 1))
    }

    private companion object {
        fun GraphEntryPoint.Factory.asTypeSpecBuilder(resolver: TypeResolver) = TypeSpec
            .objectBuilder(resolver.transformClassName(type))
    }
}