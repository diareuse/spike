package spike.compiler.androidx

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.compiler.conversion.ConversionProblem
import spike.compiler.conversion.ProblemReporter

@OptIn(KspExperimental::class)
class SymbolProcessorUnlessAmbiguous(
    private val packageName: String,
    private val code: String,
    private val message: String,
    private val reporter: ProblemReporter,
    private val delegate: SymbolProcessor,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val markers = resolver.getDeclarationsFromPackage(packageName)
            .filterIsInstance<KSClassDeclaration>()
            .filter { it.simpleName.asString().startsWith(ViewModelNames.EntryPointMarkerPrefix) }
            .filter { it.containingFile == null }
            .toList()
        if (markers.size < 2) return delegate.process(resolver)
        val modules = markers.map { it.simpleName.asString().removePrefix(ViewModelNames.EntryPointMarkerPrefix) }
        for (marker in markers) {
            reporter.report(ConversionProblem(code, "$message Modules: ${modules.sorted().joinToString()}.", marker))
        }
        return emptyList()
    }
}
