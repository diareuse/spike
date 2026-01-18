package spike.compiler.generator.container

import com.squareup.kotlinpoet.FileSpec
import spike.compiler.generator.FileGenerator
import spike.compiler.generator.FileGeneratorChain
import spike.compiler.generator.TypeResolver
import spike.graph.DependencyGraph
import spike.graph.GraphEntryPoint

data class DependencyContainerFileChain(
    override val subject: DependencyGraph,
    private val generators: List<FileGenerator<DependencyGraph>>,
    override val resolver: TypeResolver,
    override val spec: FileSpec.Builder = subject.entry.asFileSpecBuilder(resolver),
    private val index: Int = 0
) : FileGeneratorChain<DependencyGraph> {
    override fun proceed(): FileSpec.Builder {
        if (index == generators.size) return spec
        return generators[index].generate(copy(index = index + 1))
    }

    private companion object {
        fun GraphEntryPoint.asFileSpecBuilder(resolver: TypeResolver) = FileSpec
            .builder(resolver.getDependencyContainerClassName(type))
    }
}