package spike.compiler.jsr

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import spike.EntryPoint
import spike.Include
import spike.compiler.conversion.Conversion
import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionCheckFirst
import spike.compiler.conversion.ConversionCheckMapped
import spike.compiler.conversion.IncludeConversionFile
import spike.compiler.conversion.IncludeFunctionConstructor
import spike.compiler.conversion.Injection
import spike.compiler.conversion.InjectionCheckConstructor
import spike.compiler.conversion.InjectionCheckGeneric
import spike.compiler.conversion.InjectionCheckQualifier
import spike.compiler.conversion.InjectionCheckVisibility
import spike.compiler.conversion.IncludeFunctionInternal
import spike.compiler.conversion.IncludeFunctionNamed
import spike.compiler.conversion.IncludeFunctionOriginating
import spike.compiler.conversion.IncludeFunctionScoped
import spike.compiler.conversion.NameLookup
import spike.compiler.conversion.NameLookupAnnotation
import spike.compiler.conversion.ParameterConversion
import spike.compiler.conversion.ParameterConversionDirect
import spike.compiler.conversion.ParameterConversionNamed
import spike.compiler.conversion.ParameterConversionWrapped
import spike.compiler.conversion.ProblemReporter
import spike.compiler.conversion.ProblemReporterDistinct
import spike.compiler.conversion.ProblemReporterLogger
import spike.compiler.conversion.QualifierLookup
import spike.compiler.conversion.QualifierLookupMeta
import spike.compiler.conversion.ScopeLookup
import spike.compiler.conversion.ScopeLookupAnnotation
import spike.compiler.conversion.SpikeNames
import spike.compiler.conversion.SymbolProcessorConversion
import spike.compiler.conversion.SymbolProcessorOnce
import spike.compiler.conversion.SymbolSourceAnnotated

@EntryPoint
interface JsrConversion {
    val processor: SymbolProcessor

    @EntryPoint.Factory
    interface Factory {
        fun create(environment: SymbolProcessorEnvironment): JsrConversion
    }

    companion object
}

@Include
fun jsrProcessor(
    check: ConversionCheck<KSAnnotated>,
    conversion: Conversion<KSAnnotated>,
    reporter: ProblemReporter,
): SymbolProcessor = SymbolProcessorOnce(
    SymbolProcessorConversion(SymbolSourceAnnotated(JsrNames.Inject), check, conversion, reporter),
)

@Include
fun jsrReporter(environment: SymbolProcessorEnvironment): ProblemReporter =
    ProblemReporterDistinct(ProblemReporterLogger(environment.logger))

@Include
fun jsrNames(): NameLookup = NameLookupAnnotation(JsrNames.Named)

@Include
fun jsrScopes(): ScopeLookup = ScopeLookupAnnotation(JsrNames.Singleton, JsrNames.Scope)

@Include
fun jsrQualifiers(): QualifierLookup = QualifierLookupMeta(JsrNames.Qualifier, converted = JsrNames.Named)

@Include
fun jsrCheck(qualifiers: QualifierLookup): ConversionCheck<KSAnnotated> = ConversionCheckFirst(
    listOf(
        InjectionCheckConstructor(
            "SPIKE-JSR-FIELD-INJECTION",
            "Spike injects only constructors; move this dependency into the @Inject constructor.",
        ),
        ConversionCheckMapped(
            Injection::of,
            ConversionCheckFirst(
                listOf(
                    InjectionCheckSingleConstructor(JsrNames.Inject),
                    InjectionCheckVisibility("SPIKE-JSR-VISIBILITY"),
                    InjectionCheckGeneric("SPIKE-JSR-GENERIC"),
                    InjectionCheckQualifier(
                        qualifiers,
                        "SPIKE-JSR-QUALIFIER",
                        "Only @Named is converted; " +
                            "annotate this qualifier with @spike.Qualifier instead of a JSR-330 one.",
                    ),
                ),
            ),
        ),
    ),
)

@Include
fun jsrParameters(names: NameLookup): ParameterConversion = ParameterConversionNamed(
    names,
    JsrNames.SpikeNamed,
    ParameterConversionWrapped(JsrNames.Provider, SpikeNames.Provider, "get()", ParameterConversionDirect()),
)

@Include
fun jsrConversion(
    parameters: ParameterConversion,
    names: NameLookup,
    scopes: ScopeLookup,
    environment: SymbolProcessorEnvironment,
): Conversion<KSAnnotated> {
    val core = IncludeFunctionConstructor(SpikeNames.Include, parameters)
    val function = IncludeFunctionOriginating(
        IncludeFunctionNamed(
            names,
            JsrNames.SpikeNamed,
            IncludeFunctionScoped(scopes, SpikeNames.Singleton, IncludeFunctionInternal(core)),
        ),
    )
    return IncludeConversionFile(Injection::of, function, environment.codeGenerator)
}
