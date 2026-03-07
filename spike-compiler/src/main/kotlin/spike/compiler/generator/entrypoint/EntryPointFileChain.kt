package spike.compiler.generator.entrypoint

import com.squareup.kotlinpoet.FileSpec
import spike.compiler.generator.FileGenerator
import spike.compiler.generator.FileGeneratorChain
import spike.compiler.generator.TypeResolver
import spike.compiler.graph.GraphEntryPoint

class EntryPointFileChain(
    override val subject: GraphEntryPoint,
    private val generators: List<FileGenerator<GraphEntryPoint>>,
    override val resolver: TypeResolver,
) : FileGeneratorChain<GraphEntryPoint> {
    override val spec: FileSpec.Builder = subject.asFileSpecBuilder(resolver)
    fun proceed() = generators[0].generate(this)
    override fun proceed(origin: FileGenerator<GraphEntryPoint>): FileSpec.Builder {
        val index = generators.indexOf(origin) + 1
        if (index == generators.size) return spec
        return generators[index].generate(this)
    }

    private companion object {
        fun GraphEntryPoint.asFileSpecBuilder(resolver: TypeResolver) = FileSpec
            .builder(resolver.transformClassName(type))
    }
}
