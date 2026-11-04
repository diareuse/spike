package spike.compiler.processor.symbol

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

class SymbolProcessorImmediate(
    private val delegate: SymbolProcessor,
    private val marker: SettleMarker,
) : SymbolProcessor {

    private var round = 0

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (++round == 1) marker.write(round)
        return delegate.process(resolver)
    }

    override fun finish() = delegate.finish()

    override fun onError() = delegate.onError()
}
