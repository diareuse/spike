package spike.compiler.processor.symbol

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSDeclaration
import spike.compiler.diagnostic.DiagnosticGeneratedAfterGraph
import spike.compiler.diagnostic.Involved
import spike.compiler.generator.findKSFile
import spike.compiler.processor.DiagnosticReporter
import spike.compiler.processor.asOrigin
import spike.compiler.processor.described

class SymbolProcessorImmediate(
    private val delegate: SymbolProcessor,
    private val marker: SettleMarker,
    private val registry: SymbolRegistry,
    private val reporter: DiagnosticReporter,
) : SymbolProcessor {

    private var round = 0
    private var built = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (++round == 1) marker.write(round)
        if (built) reportLate(resolver)
        return delegate.process(resolver).also { built = it.isEmpty() }
    }

    private fun reportLate(resolver: Resolver) {
        val newFiles = resolver.getNewFiles().toSet()
        val involved = registry.all(resolver)
            .filter { it.findKSFile() in newFiles }
            .filterIsInstance<KSDeclaration>()
            .map { Involved(it.described, it.asOrigin()) }
        if (involved.isNotEmpty()) reporter.report(DiagnosticGeneratedAfterGraph(involved))
    }

    override fun finish() = delegate.finish()

    override fun onError() = delegate.onError()
}
