package spike.compiler.processor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import spike.Include
import spike.compiler.generator.DependencyGraphGenerator
import spike.compiler.processor.symbol.SymbolRegistry

fun interface GraphContributor {
    fun contribute(context: GraphContext)

    companion object {
        @Include
        fun default(
            viewModel: IncludeContributorViewModel,
            registry: SymbolRegistry,
            include: IncludeContributor,
            environment: SymbolProcessorEnvironment
        ): GraphContributor = GraphContributorMeta(
            listOf(
                GraphContributorIncludeViewModel(viewModel, registry)
                    .timed("ViewModel", environment.logger),
                GraphContributorInclude(include, registry)
                    .timed("Include", environment.logger),
                GraphContributorEntryPoint(
                    generator = DependencyGraphGenerator(false),
                    environment = environment,
                    logger = environment.logger,
                    registry = registry
                ).timed("EntryPoint", environment.logger),
                GraphContributorExport(
                    generator = DependencyGraphGenerator(true),
                    environment = environment,
                    logger = environment.logger,
                    registry = registry
                ).timed("Export", environment.logger)
            )
        )

        private fun GraphContributor.timed(tag: String, logger: KSPLogger): GraphContributor {
            return GraphContributorMeasureTime(this, logger, tag)
        }

    }
}

private class GraphContributorMeta(
    private val contributors: List<GraphContributor>,
) : GraphContributor {
    override fun contribute(
        context: GraphContext
    ) {
        for (contributor in contributors) {
            contributor.contribute(context)
        }
    }
}
