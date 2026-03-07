package spike.compiler.generator.entrypoint.factory

import com.squareup.kotlinpoet.TypeSpec
import spike.compiler.generator.TypeGenerator
import spike.compiler.generator.TypeGeneratorChain
import spike.compiler.generator.TypeResolver
import spike.compiler.graph.GraphEntryPoint

class EntryPointFactoryTypeChain(
    override val subject: GraphEntryPoint.Factory,
    private val generators: List<TypeGenerator<GraphEntryPoint.Factory>>,
    override val resolver: TypeResolver,
) : TypeGeneratorChain<GraphEntryPoint.Factory> {
    override val spec: TypeSpec.Builder = subject.asTypeSpecBuilder(resolver)
    fun proceed() = generators[0].generate(this)
    override fun proceed(origin: TypeGenerator<GraphEntryPoint.Factory>): TypeSpec.Builder {
        val index = generators.indexOf(origin) + 1
        if (index == generators.size) return spec
        return generators[index].generate(this)
    }

    private companion object {
        fun GraphEntryPoint.Factory.asTypeSpecBuilder(resolver: TypeResolver) = TypeSpec
            .objectBuilder(resolver.transformClassName(type))
    }
}
