package spike.compiler.generator.entrypoint

import com.squareup.kotlinpoet.TypeSpec
import spike.compiler.generator.Generator
import spike.compiler.generator.TypeGenerator
import spike.compiler.generator.TypeGeneratorChain
import spike.compiler.generator.TypeResolver
import spike.compiler.graph.GraphEntryPoint

class EntryPointTypeChain(
    override val subject: GraphEntryPoint,
    private val generators: List<TypeGenerator<GraphEntryPoint>>,
    override val resolver: TypeResolver,
) : TypeGeneratorChain<GraphEntryPoint> {
    override val spec: TypeSpec.Builder = subject.asTypeSpecBuilder(resolver)
    fun proceed() = generators[0].generate(this)
    override fun proceed(origin: TypeGenerator<GraphEntryPoint>): TypeSpec.Builder {
        val index = generators.indexOf(origin) + 1
        if (index == generators.size) return spec
        return generators[index].generate(this)
    }

    private companion object {
        fun GraphEntryPoint.asTypeSpecBuilder(resolver: TypeResolver) = TypeSpec
            .classBuilder(resolver.transformClassName(type))
    }
}
