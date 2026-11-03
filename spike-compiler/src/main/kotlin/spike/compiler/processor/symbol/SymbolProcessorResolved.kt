package spike.compiler.processor.symbol

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import spike.compiler.diagnostic.Diagnostic
import spike.compiler.processor.DiagnosticReporter

class SymbolProcessorResolved(
    private val delegate: SymbolProcessor,
    private val registry: SymbolRegistry,
    private val unresolved: UnresolvedTypes,
    private val reporter: DiagnosticReporter,
) : SymbolProcessor {

    private var pending = emptyList<Diagnostic>()
    private var resolved = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (resolved) return delegate.process(resolver)
        val symbols = registry.all(resolver)
        pending = unresolved.of(symbols)
        resolved = pending.isEmpty()
        return if (resolved) delegate.process(resolver) else symbols
    }

    override fun finish() {
        pending.forEach(reporter::report)
        delegate.finish()
    }

    override fun onError() = delegate.onError()
}
