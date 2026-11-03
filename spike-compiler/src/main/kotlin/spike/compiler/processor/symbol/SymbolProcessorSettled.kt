package spike.compiler.processor.symbol

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import spike.BindTarget
import spike.Include
import spike.compiler.Order
import spike.compiler.generator.findKSFile

class SymbolProcessorSettled(
    private val delegate: SymbolProcessor,
    private val environment: SymbolProcessorEnvironment,
    private val registry: SymbolRegistry,
) : SymbolProcessor {

    private var round = 0
    private var settled = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        round++
        val symbols = if (settled) emptyList() else registry.all(resolver)
        settled = settled || round > MaxWaitingRounds || round > 1 && !resolver.generatedSymbolsIn(symbols)
        return if (settled) delegate.process(resolver) else waitForRound(symbols)
    }

    private fun waitForRound(symbols: List<KSAnnotated>): List<KSAnnotated> {
        environment.codeGenerator
            .createNewFile(Dependencies(true), MarkerPackage, "SpikeRound$round")
            .use { it.write("package $MarkerPackage\n".toByteArray()) }
        return symbols
    }

    override fun finish() = delegate.finish()

    override fun onError() = delegate.onError()

    private fun Resolver.generatedSymbolsIn(symbols: List<KSAnnotated>): Boolean {
        val newFiles = getNewFiles().toSet()
        return symbols.any { it.findKSFile() in newFiles }
    }

    companion object {
        private const val MarkerPackage = "spike.aggregated"
        private const val MaxWaitingRounds = 8

        @Include(bindTo = BindTarget.Map)
        @Order(20)
        fun decorator(environment: SymbolProcessorEnvironment, registry: SymbolRegistry) =
            SymbolProcessorDecorator { SymbolProcessorSettled(it, environment, registry) }
    }
}
