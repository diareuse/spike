package spike.compiler.processor.symbol

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import spike.Include
import spike.compiler.diagnostic.DiagnosticException
import spike.compiler.graph.GraphStore
import spike.compiler.graph.MultiBindingStore
import spike.compiler.processor.GraphContext
import spike.compiler.processor.DiagnosticReporter
import spike.compiler.processor.GraphContributor

@Include
@OptIn(KspExperimental::class)
class SymbolProcessorGraph(
    private val viewModel: SymbolProcessorViewModel,
    private val environment: SymbolProcessorEnvironment,
    private val contributor: GraphContributor,
    private val reporter: DiagnosticReporter,
) : SymbolProcessor {

    private var processed = false

    override fun process(resolver: Resolver) = viewModel.process(resolver).ifEmpty {
        if (processed) return@ifEmpty emptyList()
        val root = GraphStore.Builder()
        val multibind = MultiBindingStore.Builder()
        try {
            contributor.contribute(GraphContext(root, multibind, resolver))
        } catch (e: DiagnosticException) {
            e.diagnostics.forEach(reporter::report)
        } catch (@Suppress("TooGenericExceptionCaught") e: Throwable) {
            val m = e.message
            if (m != null) environment.logger.error(m)
            else environment.logger.exception(e)
            environment.logger.warn(e.stackTraceToString())
        }
        processed = true
        emptyList()
    }

}

@Include
fun symbolProcessor(
    graph: SymbolProcessorGraph,
    environment: SymbolProcessorEnvironment,
    registry: SymbolRegistry,
): SymbolProcessor = SymbolProcessorSettled(graph, environment, registry)
