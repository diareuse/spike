package spike.compiler.processor.symbol

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import spike.BindTarget
import spike.Include
import spike.compiler.Order
import spike.compiler.processor.DiagnosticReporter

class SymbolProcessorChosen(
    private val writers: ProcessorWriters,
    private val immediate: SymbolProcessor,
    private val settled: SymbolProcessor,
    private val logger: KSPLogger,
) : SymbolProcessor {

    private var chosen: SymbolProcessor? = null

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val processor = chosen ?: choose(resolver).also { chosen = it }
        return processor.process(resolver)
    }

    private fun choose(resolver: Resolver): SymbolProcessor {
        if (writers.mayWrite(resolver)) return settled
        logger.info("Spike generated in the first KSP round because no other processor can write")
        return immediate
    }

    override fun finish() = (chosen ?: settled).finish()

    override fun onError() = (chosen ?: settled).onError()

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(20)
        fun decorator(
            writers: ProcessorWriters,
            marker: SettleMarker,
            registry: SymbolRegistry,
            reporter: DiagnosticReporter,
            logger: KSPLogger,
        ) = SymbolProcessorDecorator {
            SymbolProcessorChosen(
                writers = writers,
                immediate = SymbolProcessorImmediate(it, marker, registry, reporter),
                settled = SymbolProcessorSettled(it, marker, registry),
                logger = logger,
            )
        }
    }
}
