package spike.compiler.androidx

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import spike.BindTarget
import spike.EntryPoint
import spike.Include
import spike.compiler.conversion.IncludeConversionFile
import spike.compiler.conversion.IncludeFunctionOriginating
import spike.compiler.conversion.Order
import spike.compiler.conversion.ParameterConversion
import spike.compiler.conversion.ParameterConversionDirect
import spike.compiler.conversion.SymbolProcessorAll
import spike.compiler.conversion.SymbolProcessorOnce
import spike.compiler.conversion.SymbolProcessorRequiring
import spike.compiler.conversion.SymbolProcessorUnchecked
import spike.compiler.conversion.SymbolSourceAnnotated
import spike.compiler.conversion.SymbolSourceFiltered
import spike.compiler.conversion.ordered

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
fun viewModelProcessor(processors: Map<Int, SymbolProcessor>): SymbolProcessor =
    SymbolProcessorOnce(SymbolProcessorAll(processors.ordered()))

@Include(bindTo = BindTarget.Map)
@Order(10)
fun viewModelIncludes(function: IncludeFunctionViewModel, codeGenerator: CodeGenerator): SymbolProcessor {
    val viewModels = SymbolSourceFiltered(SymbolSourceAnnotated(ViewModelNames.SpikeViewModel)) {
        ViewModelInclusion.of(it) != null
    }
    val conversion = IncludeConversionFile(ViewModelInclusion::of, IncludeFunctionOriginating(function), codeGenerator)
    return SymbolProcessorUnchecked(viewModels, conversion)
}

@Include(bindTo = BindTarget.Map)
@Order(20)
fun viewModelEntryPoint(environment: SymbolProcessorEnvironment): SymbolProcessor {
    val packageName = environment.options["spike.viewmodel.package"] ?: "spike.lifecycle.viewmodel"
    return SymbolProcessorEnabledBy(
        environment.options,
        "spike.viewmodel.enabled",
        SymbolProcessorUnlessExported(
            SymbolSourceAnnotated(ViewModelNames.Export),
            SymbolProcessorRequiring(
                ViewModelNames.ViewModelEntryPoint,
                SymbolProcessorViewModelEntryPoint(packageName, environment.codeGenerator),
            ),
        ),
    )
}

@Include
fun viewModelParameters(): ParameterConversion =
    ParameterConversionQualified(ViewModelNames.Qualifier, ParameterConversionDirect())

@Include
fun SymbolProcessorEnvironment.viewModelCodeGenerator(): CodeGenerator = codeGenerator
