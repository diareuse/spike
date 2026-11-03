package spike.compiler.androidx

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import spike.compiler.conversion.ConversionProblem
import spike.compiler.conversion.ProblemReporter
import spike.compiler.conversion.SymbolSource

class SymbolProcessorRejectingStrandedViewModels(
    private val entryPoint: String,
    private val viewModels: SymbolSource,
    private val markers: EntryPointMarkers,
    private val reporter: ProblemReporter,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val module = markers.module(resolver)
        for (viewModel in viewModels.symbols(resolver)) reporter.report(problem(module, viewModel))
        return emptyList()
    }

    private fun problem(module: CompiledModule, viewModel: KSAnnotated) = ConversionProblem(
        "SPIKE-VIEWMODEL-ENTRY-POINT",
        "$entryPoint is already compiled in ${module.origin}, so this ViewModel reaches no entry point. If " +
            "${module.subject} is a " +
            "library, mark it as one: spike { library = true } with the io.github.diareuse.spike Gradle plugin, " +
            "the KSP option spike.library=true, or an @Export interface in it. It then leaves the entry point to " +
            "this module and exports its ViewModels. If ${module.subject} is this module's main source set (a test " +
            "compilation), move the ViewModel there.",
        viewModel,
    )
}
