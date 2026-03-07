package spike.compiler.generator.entrypoint.factory

import com.squareup.kotlinpoet.FileSpec
import spike.compiler.generator.FileGenerator
import spike.compiler.generator.FileGeneratorChain
import spike.compiler.generator.TypeResolver
import spike.compiler.graph.GraphEntryPoint

class EntryPointFactoryFileChain(
    override val subject: GraphEntryPoint.Factory,
    private val generators: List<FileGenerator<GraphEntryPoint.Factory>>,
    override val resolver: TypeResolver,
) : FileGeneratorChain<GraphEntryPoint.Factory> {
    override val spec: FileSpec.Builder = subject.asFileSpecBuilder(resolver)
    fun proceed() = generators[0].generate(this)
    override fun proceed(origin: FileGenerator<GraphEntryPoint.Factory>): FileSpec.Builder {
        val index = generators.indexOf(origin) + 1
        if (index == generators.size) return spec
        return generators[index].generate(this)
    }

    private companion object {
        fun GraphEntryPoint.Factory.asFileSpecBuilder(resolver: TypeResolver) = FileSpec
            .builder(resolver.transformClassName(type))
    }
}
