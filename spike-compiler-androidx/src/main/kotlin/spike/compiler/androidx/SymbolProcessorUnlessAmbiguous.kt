package spike.compiler.androidx

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import spike.compiler.conversion.ConversionProblem
import spike.compiler.conversion.ProblemReporter

class SymbolProcessorUnlessAmbiguous(
    private val markers: EntryPointMarkers,
    private val code: String,
    private val message: String,
    private val reporter: ProblemReporter,
    private val delegate: SymbolProcessor,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val compiled = markers.compiled(resolver)
        if (compiled.size < 2) return delegate.process(resolver)
        val modules = markers.modules(resolver)
        for (marker in compiled) {
            reporter.report(ConversionProblem(code, "$message Modules: ${modules.sorted().joinToString()}.", marker))
        }
        return emptyList()
    }
}
