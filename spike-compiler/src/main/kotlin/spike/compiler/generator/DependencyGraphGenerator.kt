@file:Suppress("DEPRECATION")

package spike.compiler.generator

import com.google.devtools.ksp.symbol.KSFile
import spike.compiler.graph.DependencyGraph

class DependencyGraphGenerator {

    private val resolver = TypeResolver()

    fun generate(
        graph: DependencyGraph,
        originatingFiles: List<KSFile>,
        collector: FileSpecCollector
    ) {
        val context = FileGeneratorContext(resolver, graph, originatingFiles)
        val dependencyFactoryClassName = context.resolver.peerClass(graph, "Factory")
        val entryPoint = EntryPointGenerator(dependencyFactoryClassName)
        val instructionSet = InstructionSetGenerator()
        val dependencyFactory = DependencyFactoryGenerator(
            dependencyFactoryClassName = dependencyFactoryClassName,
            instructionSet = instructionSet,
            dependencyHolder = { index -> DependencyHolderGenerator(index, dependencyFactoryClassName) },
        )
        dependencyFactory.generate(context, collector)
        entryPoint.generate(context, collector)
    }

}
