@file:Suppress("DEPRECATION")

package spike.compiler.generator

import com.google.devtools.ksp.symbol.KSFile
import spike.compiler.graph.DependencyGraph

class DependencyGraphGenerator(
    private val resolver: TypeResolver,
    private val entryPoint: Generator,
    private val members: EntryPointMembers,
    private val dependencyFactory: DependencyFactoryGenerator
) {

    fun generate(
        graph: DependencyGraph,
        originatingFiles: List<KSFile>,
        collector: FileSpecCollector
    ) {
        val context = FileGeneratorContext(resolver, graph, originatingFiles)
        dependencyFactory.generate(context, collector, members)
        entryPoint.generate(context, collector)
    }
}
