package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import spike.Include
import spike.compiler.graph.GraphStore
import spike.compiler.graph.MultiBindingStore
import spike.compiler.processor.symbol.SymbolProcessorViewModel

@Include(bindAs = SymbolProcessor::class)
@OptIn(KspExperimental::class)
class SpikeSymbolProcessor(
    private val viewModel: SymbolProcessorViewModel,
    private val environment: SymbolProcessorEnvironment,
    private val contributor: GraphContributor
) : SymbolProcessor {

    private var processed = false

    override fun process(resolver: Resolver) = viewModel.process(resolver).ifEmpty {
        if (processed) return@ifEmpty emptyList()
        val root = GraphStore.Builder()
        val multibind = MultiBindingStore.Builder()
        try {
            contributor.contribute(GraphContext(root, multibind, resolver))
        } catch (@Suppress("TooGenericExceptionCaught") e: Throwable) {
            val m = e.message
            if (m != null) environment.logger.error(m)
            else environment.logger.exception(e)
            environment.logger.logging(e.stackTraceToString())
        }
        processed = true
        emptyList()
    }

}
