package spike.compiler.processor.symbol

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

class SymbolProcessorImmediate(
    private val delegate: SymbolProcessor,
    private val marker: SettleMarker,
    private val registry: SymbolRegistry,
) : SymbolProcessor {

    private var round = 0

    override fun process(resolver: Resolver): List<KSAnnotated> = delegate.process(forRound(resolver))

    private fun forRound(resolver: Resolver): Resolver {
        if (++round > 1) return resolver
        marker.write(round)
        return ResolverDeferredView(resolver, registry.all(resolver))
    }

    override fun finish() = delegate.finish()

    override fun onError() = delegate.onError()
}
