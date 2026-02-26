package spike.compiler.generator.entrypoint

import com.squareup.kotlinpoet.FileSpec
import spike.compiler.generator.FileGenerator
import spike.compiler.generator.FileGeneratorChain
import spike.compiler.generator.TypeResolver
import spike.compiler.graph.GraphEntryPoint

data class EntryPointFileChain(
    override val subject: GraphEntryPoint,
    private val generators: List<FileGenerator<GraphEntryPoint>>,
    override val resolver: TypeResolver,
    override val spec: FileSpec.Builder = subject.asFileSpecBuilder(resolver),
    private val index: Int = 0
) : FileGeneratorChain<GraphEntryPoint> {
    override fun proceed(): FileSpec.Builder {
        if (index == generators.size) return spec
        return generators[index].generate(copy(index = index + 1))
    }

    private companion object {
        fun GraphEntryPoint.asFileSpecBuilder(resolver: TypeResolver) = FileSpec
            .builder(resolver.transformClassName(type))
    }
}