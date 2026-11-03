package spike.compiler.processor.symbol

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import spike.BindTarget
import spike.Include
import spike.compiler.Order
import spike.compiler.diagnostic.DiagnosticUnresolvedType
import spike.compiler.processor.DiagnosticReporter

class SymbolProcessorResolved(
    private val delegate: SymbolProcessor,
    private val registry: SymbolRegistry,
    private val signatures: SignatureTypes,
    private val unresolved: UnresolvedTypes,
    private val reporter: DiagnosticReporter,
) : SymbolProcessor {

    private var deferred = emptyList<KSAnnotated>()
    private var resolved = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (resolved) return delegate.process(resolver)
        val symbols = registry.all(resolver)
        deferred = if (unresolvedIn(symbols).any()) symbols else emptyList()
        resolved = deferred.isEmpty()
        return if (resolved) delegate.process(resolver) else deferred
    }

    override fun finish() {
        for (reference in unresolvedIn(deferred).distinctBy { listOf(it.type, it.user.description, it.user.origin?.location) }) {
            reporter.report(DiagnosticUnresolvedType(reference.type, listOf(reference.user)))
        }
        delegate.finish()
    }

    override fun onError() = delegate.onError()

    private fun unresolvedIn(symbols: List<KSAnnotated>) =
        unresolved.of(symbols.asSequence().flatMap(signatures::of))

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(10)
        fun decorator(
            registry: SymbolRegistry,
            signatures: SignatureTypes,
            unresolved: UnresolvedTypes,
            reporter: DiagnosticReporter,
        ) = SymbolProcessorDecorator { SymbolProcessorResolved(it, registry, signatures, unresolved, reporter) }
    }
}
