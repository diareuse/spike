package spike.compiler.generator

import com.squareup.kotlinpoet.FileSpec
import spike.graph.GraphEntryPoint

data class EntryPointFactoryFileChain(
    override val subject: GraphEntryPoint.Factory,
    private val generators: List<FileGenerator<GraphEntryPoint.Factory>>,
    override val resolver: TypeResolver,
    override val spec: FileSpec.Builder = subject.asFileSpecBuilder(resolver),
    private val index: Int = 0
) : FileGeneratorChain<GraphEntryPoint.Factory> {
    override fun proceed(): FileSpec.Builder {
        if (index == generators.size) return spec
        return generators[index].generate(copy(index = index + 1))
    }

    private companion object {
        fun GraphEntryPoint.Factory.asFileSpecBuilder(resolver: TypeResolver) = FileSpec.Companion
            .builder(resolver.transformClassName(type))
    }
}
