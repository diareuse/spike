package spike.compiler.generator.entrypoint

import com.squareup.kotlinpoet.TypeSpec
import spike.compiler.generator.TypeGenerator
import spike.compiler.generator.TypeGeneratorChain
import spike.compiler.generator.TypeResolver
import spike.graph.GraphEntryPoint

data class EntryPointTypeChain(
    override val subject: GraphEntryPoint,
    private val generators: List<TypeGenerator<GraphEntryPoint>>,
    override val resolver: TypeResolver,
    override val spec: TypeSpec.Builder = subject.asTypeSpecBuilder(resolver),
    private val index: Int = 0
) : TypeGeneratorChain<GraphEntryPoint> {
    override fun proceed(): TypeSpec.Builder {
        if (index == generators.size) return spec
        return generators[index].generate(copy(index = index + 1))
    }

    private companion object {
        fun GraphEntryPoint.asTypeSpecBuilder(resolver: TypeResolver) = TypeSpec
            .classBuilder(resolver.transformClassName(type))
    }
}