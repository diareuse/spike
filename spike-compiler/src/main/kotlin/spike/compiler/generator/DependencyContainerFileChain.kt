package spike.compiler.generator

import com.squareup.kotlinpoet.FileSpec
import spike.graph.DependencyGraph
import spike.graph.GraphEntryPoint

data class DependencyContainerFileChain(
    override val subject: DependencyGraph,
    private val generators: List<FileGenerator<DependencyGraph>>,
    override val resolver: TypeResolver,
    override val spec: FileSpec.Builder = subject.entry.asFileSpecBuilder(),
    private val index: Int = 0
) : FileGeneratorChain<DependencyGraph> {
    override fun proceed(): FileSpec.Builder {
        if (index == generators.size) return spec
        return generators[index].generate(copy(index = index + 1))
    }

    private companion object {
        fun GraphEntryPoint.asFileSpecBuilder() = FileSpec.Companion
            .builder(type.toClassName().peerClass { "Spike${it.simpleName}DependencyContainer" })
    }
}