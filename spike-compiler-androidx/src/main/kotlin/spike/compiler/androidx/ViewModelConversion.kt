package spike.compiler.androidx

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import spike.EntryPoint
import spike.Include
import spike.compiler.conversion.IncludeConversionFile
import spike.compiler.conversion.IncludeFunctionOriginating
import spike.compiler.conversion.ParameterConversionDirect
import spike.compiler.conversion.SymbolProcessorAll
import spike.compiler.conversion.SymbolProcessorOnce
import spike.compiler.conversion.SymbolProcessorRequiring
import spike.compiler.conversion.SymbolProcessorUnchecked
import spike.compiler.conversion.SymbolSourceAnnotated
import spike.compiler.conversion.SymbolSourceFiltered

@EntryPoint
interface ViewModelConversion {
    val processor: SymbolProcessor

    @EntryPoint.Factory
    interface Factory {
        fun create(environment: SymbolProcessorEnvironment): ViewModelConversion
    }

    companion object
}

@Include
fun viewModelProcessor(environment: SymbolProcessorEnvironment): SymbolProcessor {
    val viewModels = SymbolSourceFiltered(SymbolSourceAnnotated(ViewModelNames.SpikeViewModel)) {
        ViewModelInclusion.of(it) != null
    }
    val parameters = ParameterConversionQualified(ViewModelNames.Qualifier, ParameterConversionDirect())
    val function = IncludeFunctionOriginating(IncludeFunctionViewModel(parameters))
    val includes = SymbolProcessorUnchecked(
        viewModels,
        IncludeConversionFile(ViewModelInclusion::of, function, environment.codeGenerator),
    )
    val enabled = environment.options["spike.viewmodel.enabled"]?.toBoolean() ?: true
    if (!enabled) return SymbolProcessorOnce(includes)
    val packageName = environment.options["spike.viewmodel.package"] ?: "spike.lifecycle.viewmodel"
    val entryPoint = SymbolProcessorUnlessExported(
        SymbolSourceAnnotated(ViewModelNames.Export),
        SymbolProcessorRequiring(
            ViewModelNames.ViewModelEntryPoint,
            SymbolProcessorViewModelEntryPoint(packageName, environment.codeGenerator),
        ),
    )
    return SymbolProcessorOnce(SymbolProcessorAll(listOf(includes, entryPoint)))
}
