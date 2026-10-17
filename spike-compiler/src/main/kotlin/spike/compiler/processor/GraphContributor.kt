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
            registry: SymbolRegistry,
            include: IncludeContributor,
            environment: SymbolProcessorEnvironment,
            generator: DependencyGraphGenerator.Factory,
            reporter: DiagnosticReporter,
            warnings: WarningReporter,
        ): GraphContributor = GraphContributorMeta(
            listOf(
                GraphContributorInclude(include, registry)
                    .timed("Include", environment.logger),
                GraphContributorEntryPoint(
                    generator = generator.create(false),
                    environment = environment,
                    logger = environment.logger,
                    reporter = reporter,
                    warnings = warnings,
                    contributions = ContributionImportsGenerated(),
                    registry = registry,
                    stores = EntryPointStoresDerived(
                        delegate = EntryPointStoresLocal,
                        production = ProductionGraph(
                            IndexedDeclarations(
                                mapOf(
                                    EntryPointIndex.CLASS to IndexLookupClass(),
                                    EntryPointIndex.FUNCTION to IndexLookupFunction(),
                                    EntryPointIndex.PROPERTY to IndexLookupProperty(),
                                ),
                            ),
                            IncludeVisitor(include),
                        ),
                        registry = registry,
                        replacements = ReplacementReader(ProvidedTypeVisitor()),
                        check = ReplacementCheckAll(listOf(ReplacementCheckAssignable(), ReplacementCheckBound())),
                    ),
                ).timed("EntryPoint", environment.logger),
                GraphContributorExport(
                    generator = generator.create(true),
                    environment = environment,
                    logger = environment.logger,
                    reporter = reporter,
                    warnings = warnings,
                    contributions = ContributionImportsGenerated(),
                    registry = registry
                ).timed("Export", environment.logger),
                GraphContributorIndex(registry, environment).timed("Index", environment.logger),
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
