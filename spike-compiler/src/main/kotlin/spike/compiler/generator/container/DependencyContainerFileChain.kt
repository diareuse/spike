package spike.compiler.generator.container

import com.squareup.kotlinpoet.FileSpec
import spike.compiler.generator.FileGenerator
import spike.compiler.generator.FileGeneratorChain
import spike.compiler.generator.TypeResolver
import spike.compiler.graph.DependencyGraph
import spike.compiler.graph.GraphEntryPoint

class DependencyContainerFileChain(
    override val subject: DependencyGraph,
    private val generators: List<FileGenerator<DependencyGraph>>,
    override val resolver: TypeResolver,
) : FileGeneratorChain<DependencyGraph> {
    override val spec: FileSpec.Builder = subject.entry.asFileSpecBuilder(resolver)
    fun proceed() = generators[0].generate(this)
    override fun proceed(origin: FileGenerator<DependencyGraph>): FileSpec.Builder {
        val index = generators.indexOf(origin) + 1
        if (index == generators.size) return spec
        return generators[index].generate(this)
    }

    private companion object {
        fun GraphEntryPoint.asFileSpecBuilder(resolver: TypeResolver) = FileSpec
            .builder(resolver.getDependencyContainerClassName(type))
    }
}
