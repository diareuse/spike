package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import spike.compiler.generator.DependencyGraphGenerator
import spike.compiler.graph.GraphStore
import spike.compiler.graph.MultiBindingStore
import spike.compiler.processor.symbol.SymbolProcessorViewModel
import spike.compiler.processor.symbol.SymbolRegistry

@OptIn(KspExperimental::class)
class SpikeSymbolProcessor(
    private val viewModel: SymbolProcessorViewModel,
    private val environment: SymbolProcessorEnvironment,
    private val registry: SymbolRegistry
) : SymbolProcessor {

    private var processed = false

    override fun process(resolver: Resolver) = viewModel.process(resolver).ifEmpty {
        if (processed) return@ifEmpty emptyList()
        val logger = environment.logger
        var include: IncludeContributor
        include = IncludeContributorChain(
            IncludeContributorBindAs(),
            IncludeContributorMain(),
        )
        include = IncludeContributorBindTo(include)
        include = IncludeContributorMultiplatform(include, resolver)
        val viewModel = IncludeContributorViewModel()
        val contributor = GraphContributor.create {
            this += GraphContributorIncludeViewModel(viewModel, registry)
                .timed("ViewModel")
            this += GraphContributorInclude(include, registry)
                .timed("Include")
            this += GraphContributorEntryPoint(
                generator = DependencyGraphGenerator(false),
                environment = environment,
                logger = logger,
                origins = registry.entryPoint(resolver)
            ).timed("EntryPoint")
            this += GraphContributorExport(
                generator = DependencyGraphGenerator(true),
                environment = environment,
                logger = logger,
                origins = registry.export(resolver)
            ).timed("Export")
        }
        val root = GraphStore.Builder()
        val multibind = MultiBindingStore.Builder()
        try {
            contributor.contribute(GraphContext(root, multibind), resolver)
        } catch (@Suppress("TooGenericExceptionCaught") e: Throwable) {
            val m = e.message
            if (m != null) environment.logger.error(m)
            else environment.logger.exception(e)
            environment.logger.logging(e.stackTraceToString())
        }
        processed = true
        emptyList()
    }

    private fun GraphContributor.timed(tag: String): GraphContributor {
        return GraphContributorMeasureTime(this, environment, tag)
    }

}
