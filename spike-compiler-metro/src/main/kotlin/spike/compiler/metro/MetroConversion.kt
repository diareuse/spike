package spike.compiler.metro

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.BindTarget
import spike.EntryPoint
import spike.Include
import spike.compiler.conversion.AnnotationNames
import spike.compiler.conversion.NameLookup
import spike.compiler.conversion.NameLookupAnnotation
import spike.compiler.conversion.Order
import spike.compiler.conversion.ParameterConversion
import spike.compiler.conversion.ParameterConversionDirect
import spike.compiler.conversion.ParameterConversionNamed
import spike.compiler.conversion.ProblemReporter
import spike.compiler.conversion.ProblemReporterLogger
import spike.compiler.conversion.QualifierLookup
import spike.compiler.conversion.QualifierLookupMeta
import spike.compiler.conversion.ScopeLookup
import spike.compiler.conversion.ScopeLookupAnnotation
import spike.compiler.conversion.SymbolProcessorAll
import spike.compiler.conversion.SymbolProcessorOnce
import spike.compiler.conversion.SymbolProcessorRejecting
import spike.compiler.conversion.SymbolSource
import spike.compiler.conversion.SymbolSourceAnnotated
import spike.compiler.conversion.SymbolSourceFiltered
import spike.compiler.conversion.ordered

@EntryPoint
interface MetroConversion {
    val processor: SymbolProcessor

    @EntryPoint.Factory
    interface Factory {
        fun create(environment: SymbolProcessorEnvironment): MetroConversion
    }

    companion object
}

internal const val UNSUPPORTED_CODE = "SPIKE-METRO-UNSUPPORTED"
internal const val UNSUPPORTED_MESSAGE = "This Metro declaration cannot be converted."
internal const val QUALIFIER_CODE = "SPIKE-METRO-QUALIFIER"
internal const val QUALIFIER_MESSAGE =
    "Only @Named is converted; annotate this qualifier with @spike.Qualifier instead."
internal const val PROVIDER_CODE = "SPIKE-METRO-PROVIDER"
internal const val PROVIDER_MESSAGE = "Metro's Provider is not converted; request spike.Provider or Lazy instead."

internal fun classesWith(names: AnnotationNames): SymbolSource =
    SymbolSourceFiltered(SymbolSourceAnnotated(names)) { it is KSClassDeclaration }

@Include
fun metroProcessor(processors: Map<Int, SymbolProcessor>): SymbolProcessor =
    SymbolProcessorOnce(SymbolProcessorAll(processors.ordered()))

@Include(bindTo = BindTarget.Map)
@Order(70)
fun metroExtensions(reporter: ProblemReporter): SymbolProcessor = SymbolProcessorRejecting(
    SymbolSourceAnnotated(MetroNames.GraphExtension),
    "SPIKE-METRO-EXTENSION",
    "Graph extensions are not converted; Spike has no graph hierarchy, so use a separate entry point.",
    reporter,
)

@Include
fun metroReporter(logger: KSPLogger): ProblemReporter = ProblemReporterLogger(logger)

@Include
fun SymbolProcessorEnvironment.metroLogger(): KSPLogger = logger

@Include
fun SymbolProcessorEnvironment.metroCodeGenerator(): CodeGenerator = codeGenerator

@Include
fun metroNames(): NameLookup = NameLookupAnnotation(MetroNames.Named)

@Include
fun metroQualifiers(): QualifierLookup = QualifierLookupMeta(MetroNames.Qualifier, converted = MetroNames.Named)

@Include
fun metroScopes(): ScopeLookup = ScopeLookupAnnotation(MetroNames.SingleIn, MetroNames.Scope)

@Include
fun metroParameters(names: NameLookup): ParameterConversion =
    ParameterConversionNamed(names, MetroNames.SpikeNamed, ParameterConversionDirect())
