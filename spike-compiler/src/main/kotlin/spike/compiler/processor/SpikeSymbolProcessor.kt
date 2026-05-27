package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import spike.EntryPoint
import spike.Export
import spike.Include
import spike.compiler.generator.DependencyGraphGenerator
import spike.compiler.graph.GraphStore
import spike.compiler.graph.MultiBindingStore
import spike.compiler.processor.symbol.SymbolProcessorViewModel
import spike.compiler.processor.util.getSymbolsWithAnnotation

@OptIn(KspExperimental::class)
class SpikeSymbolProcessor(
    private val viewModel: SymbolProcessorViewModel,
    private val environment: SymbolProcessorEnvironment,
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        viewModel.process(resolver)
        when (viewModel.round) {
            1 -> return buildList {
                this += resolver.getSymbolsWithAnnotation("spike.lifecycle.viewmodel.SpikeViewModel")
                this += resolver.getSymbolsWithAnnotation<Include>()
                this += resolver.getSymbolsWithAnnotation<EntryPoint>()
                this += resolver.getSymbolsWithAnnotation<Export>()
            }
        }
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
            this += GraphContributorIncludeViewModel(viewModel)
                .timed("ViewModel")
            this += GraphContributorIncludeClass(include)
                .timed("Class")
            this += GraphContributorIncludeFunction(include)
                .timed("Function")
            this += GraphContributorEntryPoint(
                generator = DependencyGraphGenerator(false),
                environment = environment,
                logger = logger,
                origins = resolver.getSymbolsWithAnnotation<EntryPoint>()
            ).timed("EntryPoint")
            this += GraphContributorExport(
                generator = DependencyGraphGenerator(true),
                environment = environment,
                logger = logger,
                origins = resolver.getSymbolsWithAnnotation<Export>()
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
        return emptyList()
    }

    private fun GraphContributor.timed(tag: String): GraphContributor {
        return GraphContributorMeasureTime(this, environment, tag)
    }

    private companion object {
        private const val ViewModel = "spike.lifecycle.viewmodel"
    }

}
