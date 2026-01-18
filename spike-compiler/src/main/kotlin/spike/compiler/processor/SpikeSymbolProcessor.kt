package spike.compiler.processor

import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.*
import spike.compiler.generator.*
import spike.graph.*

@OptIn(KspExperimental::class)
class SpikeSymbolProcessor(
    private val environment: SymbolProcessorEnvironment
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val builder = DependencyGraph.Builder()
        val bindAs = IncludeBindAsContributorImpl()
        val generator = DependencyGraphGenerator(environment)
        val contributor = GraphContributor.create {
            this += GraphContributorIncludeClass(bindAs)
            this += GraphContributorIncludeFunction(bindAs)
            this += GraphContributorEntryPoint(generator)
        }
        contributor.contribute(GraphContext(builder), resolver)
        return emptyList()
    }

}
