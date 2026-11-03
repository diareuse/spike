package spike.compiler.androidx

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
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
import spike.compiler.conversion.ProblemReporter
import spike.compiler.conversion.ProblemReporterDistinct
import spike.compiler.conversion.ProblemReporterLogger
import spike.compiler.conversion.SymbolProcessorAll
import spike.compiler.conversion.SymbolProcessorUnlessCompiled
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
fun viewModelEntryPoint(environment: SymbolProcessorEnvironment, reporter: ProblemReporter): SymbolProcessor {
    val packageName = environment.options["spike.viewmodel.package"] ?: "spike.lifecycle.viewmodel"
    val name = "$packageName.${ViewModelNames.ViewModelEntryPointImpl}"
    val markers = EntryPointMarkers(packageName)
    val identity = ModuleIdentity(environment.options)
    return SymbolProcessorEnabledBy(
        environment.options,
        "spike.viewmodel.enabled",
        SymbolProcessorUnlessExported(
            SymbolSourceAnnotated(ViewModelNames.Export),
            SymbolProcessorModuleRole(
                environment.options,
                library = SymbolProcessorViewModelExport(
                    SymbolSourceAnnotated(ViewModelNames.SpikeViewModel),
                    environment.codeGenerator,
                    identity,
                ),
                app = SymbolProcessorRequiring(
                    ViewModelNames.ViewModelEntryPoint,
                    SymbolProcessorUnlessAmbiguous(
                        markers,
                        "SPIKE-VIEWMODEL-ENTRY-POINT-AMBIGUOUS",
                        "$name is compiled in more than one dependency, so which ViewModels this module sees " +
                            "depends on classpath order. Leave the entry point to the app: mark each library " +
                            "module with spike { library = true } (io.github.diareuse.spike Gradle plugin), the " +
                            "KSP option spike.library=true, or an @Export interface in it.",
                        reporter,
                        SymbolProcessorUnlessCompiled(
                            name,
                            SymbolProcessorAll(
                                listOf(
                                    SymbolProcessorRejectingStrandedViewModels(
                                        name,
                                        SymbolSourceAnnotated(ViewModelNames.SpikeViewModel),
                                        markers,
                                        reporter,
                                    ),
                                    SymbolProcessorRejectingUnreachedExports(name, markers, reporter),
                                ),
                            ),
                            SymbolProcessorAll(
                                listOf(
                                    SymbolProcessorViewModelEntryPoint(packageName, environment.codeGenerator),
                                    SymbolProcessorViewModelEntryPointMarker(packageName, environment.codeGenerator, identity),
                                ),
                            ),
                        ),
                    ),
                ),
            ),
        ),
    )
}

@Include
fun viewModelReporter(logger: KSPLogger): ProblemReporter = ProblemReporterDistinct(ProblemReporterLogger(logger))

@Include
fun SymbolProcessorEnvironment.viewModelLogger(): KSPLogger = logger

@Include
fun viewModelParameters(): ParameterConversion =
    ParameterConversionQualified(ViewModelNames.Qualifier, ParameterConversionDirect())

@Include
fun SymbolProcessorEnvironment.viewModelCodeGenerator(): CodeGenerator = codeGenerator
