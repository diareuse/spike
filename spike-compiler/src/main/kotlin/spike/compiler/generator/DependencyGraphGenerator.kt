@file:Suppress("DEPRECATION")

package spike.compiler.generator

import com.google.devtools.ksp.symbol.KSFile
import spike.Include
import spike.compiler.graph.DependencyGraph

class DependencyGraphGenerator private constructor(
    private val resolver: TypeResolver,
    private val entryPoint: Generator,
    private val dependencyFactory: DependencyFactoryGenerator
) {

    fun generate(
        graph: DependencyGraph,
        originatingFiles: List<KSFile>,
        collector: FileSpecCollector
    ) {
        val context = FileGeneratorContext(resolver, graph, originatingFiles)
        dependencyFactory.generate(context, collector)
        entryPoint.generate(context, collector)
    }

    @Include
    class Factory(
        private val resolver: TypeResolver,
        private val dependencyFactory: DependencyFactoryGenerator
    ) {
        fun create(export: Boolean) = DependencyGraphGenerator(
            resolver = resolver,
            entryPoint = when (export) {
                true -> ExportGenerator()
                else -> EntryPointGenerator()
            },
            dependencyFactory = dependencyFactory
        )
    }

}
