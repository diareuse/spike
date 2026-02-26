package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import spike.EntryPoint
import spike.compiler.generator.DependencyGraphGenerator
import spike.compiler.processor.util.getSymbolsWithAnnotation
import spike.compiler.graph.GraphStore
import spike.compiler.graph.MultiBindingStore

@OptIn(KspExperimental::class)
class SpikeSymbolProcessor(
    private val environment: SymbolProcessorEnvironment
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val bindAs = IncludeContributorBindTo(
            IncludeContributorChain(
                IncludeContributorBindAs(),
                IncludeContributorMain()
            )
        )
        val viewModel = IncludeContributorViewModel()
        val generator = DependencyGraphGenerator(environment)
        val contributor = GraphContributor.create {
            this += GraphContributorIncludeViewModel(viewModel)
            this += GraphContributorIncludeClass(bindAs)
            this += GraphContributorIncludeFunction(bindAs)
            this += GraphContributorEntryPoint(generator) { it.getSymbolsWithAnnotation<EntryPoint>() }
            this += GraphContributorEntryPoint(generator) { it.getSymbolsWithAnnotation<EntryPoint>("spike.lifecycle.viewmodel") }
        }
        val root = GraphStore.Builder()
        val multibind = MultiBindingStore.Builder()
        contributor.contribute(GraphContext(root, multibind), resolver)
        return emptyList()
    }

}
