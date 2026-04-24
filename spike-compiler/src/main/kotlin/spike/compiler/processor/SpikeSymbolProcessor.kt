package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import spike.EntryPoint
import spike.compiler.generator.DependencyGraphGenerator
import spike.compiler.graph.GraphStore
import spike.compiler.graph.MultiBindingStore
import spike.compiler.processor.util.getSymbolsWithAnnotation
import java.util.concurrent.atomic.AtomicBoolean

@OptIn(KspExperimental::class)
class SpikeSymbolProcessor(
    private val environment: SymbolProcessorEnvironment,
) : SymbolProcessor {

    private val processed = AtomicBoolean(false)

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (processed.getAndSet(true)) return emptyList()
        val logger = environment.logger
        val bindAs = IncludeContributorBindTo(
            IncludeContributorChain(
                IncludeContributorBindAs(),
                IncludeContributorMain(),
            ),
        )
        val viewModel = IncludeContributorViewModel()
        val generator = DependencyGraphGenerator()
        val contributor = GraphContributor.create {
            this += GraphContributorIncludeViewModel(viewModel)
                .timed("ViewModel")
            this += GraphContributorIncludeClass(bindAs)
                .timed("Class")
            this += GraphContributorIncludeFunction(bindAs)
                .timed("Function")
            this += GraphContributorEntryPoint(generator, environment, logger) {
                it.getSymbolsWithAnnotation<EntryPoint>()
            }.timed("EntryPoint")
            this += GraphContributorEntryPoint(generator, environment, logger) {
                it.getSymbolsWithAnnotation<EntryPoint>(ViewModel)
            }.timed("ViewModelEntryPoint")
        }
        val root = GraphStore.Builder()
        val multibind = MultiBindingStore.Builder()
        contributor.contribute(GraphContext(root, multibind), resolver)
        return emptyList()
    }

    private fun GraphContributor.timed(tag: String): GraphContributor {
        return GraphContributorMeasureTime(this, environment, tag)
    }

    private companion object {
        private const val ViewModel = "spike.lifecycle.viewmodel"
    }

}
