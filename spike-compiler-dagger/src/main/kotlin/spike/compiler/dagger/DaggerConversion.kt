package spike.compiler.dagger

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import spike.EntryPoint
import spike.Include
import spike.compiler.conversion.ConversionCheckFirst
import spike.compiler.conversion.ConversionCheckMapped
import spike.compiler.conversion.EntryPointConversionFile
import spike.compiler.conversion.EntryPointTypeInterface
import spike.compiler.conversion.EntryPointTypeOriginating
import spike.compiler.conversion.EntryPointTypeSupertype
import spike.compiler.conversion.IncludeFunctionBindTo
import spike.compiler.conversion.IncludeFunctionMapKey
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
import spike.compiler.conversion.ProblemReporterLogger
import spike.compiler.conversion.QualifierLookup
import spike.compiler.conversion.QualifierLookupMeta
import spike.compiler.conversion.ScopeLookupAnnotation
import spike.compiler.conversion.SpikeNames
import spike.compiler.conversion.SymbolProcessorAll
import spike.compiler.conversion.SymbolProcessorConversion
import spike.compiler.conversion.SymbolProcessorOnce
import spike.compiler.conversion.SymbolSourceAnnotated
import spike.compiler.jsr.JsrNames

@EntryPoint
interface DaggerConversion {
    val processor: SymbolProcessor

    @EntryPoint.Factory
    interface Factory {
        fun create(environment: SymbolProcessorEnvironment): DaggerConversion
    }

    companion object
}

@Include
fun daggerProcessor(
    names: NameLookup,
    qualifiers: QualifierLookup,
    parameters: ParameterConversion,
    reporter: ProblemReporter,
    environment: SymbolProcessorEnvironment,
): SymbolProcessor = SymbolProcessorOnce(
    SymbolProcessorAll(
        listOf(
            daggerModules(names, qualifiers, parameters, reporter, environment),
            daggerComponents(qualifiers, parameters, reporter, environment),
        ),
    ),
)

@Include
fun daggerReporter(environment: SymbolProcessorEnvironment): ProblemReporter =
    ProblemReporterLogger(environment.logger)

@Include
fun daggerNames(): NameLookup = NameLookupAnnotation(JsrNames.Named)

@Include
fun daggerQualifiers(): QualifierLookup = QualifierLookupMeta(JsrNames.Qualifier, converted = JsrNames.Named)

@Include
fun daggerParameters(names: NameLookup): ParameterConversion = ParameterConversionNamed(
    names,
    JsrNames.SpikeNamed,
    ParameterConversionWrapped(
        JsrNames.Provider,
        SpikeNames.Provider,
        "get()",
        ParameterConversionWrapped(DaggerNames.Lazy, DaggerNames.KotlinLazy, "value", ParameterConversionDirect()),
    ),
)

private fun daggerModules(
    names: NameLookup,
    qualifiers: QualifierLookup,
    parameters: ParameterConversion,
    reporter: ProblemReporter,
    environment: SymbolProcessorEnvironment,
): SymbolProcessor {
    val bindings = ConversionCheckFirst(
        listOf(
            BindingCheckVisibility(),
            BindingCheckReceiver(),
            BindingCheckMapKey(),
            BindingCheckQualifier(qualifiers),
        ),
    )
    val check = ConversionCheckMapped(
        DaggerModule::of,
        ConversionCheckFirst(listOf(ModuleCheckBindings(bindings), ModuleCheckState())),
    )
    val core = IncludeFunctionBinding(
        SpikeNames.Include,
        parameters,
        BindingCallBinds(BindingCallInstance(BindingCallStatic())),
    )
    val contributing = IncludeFunctionBindTo(
        DaggerNames.IntoSet,
        "Set",
        IncludeFunctionBindTo(DaggerNames.IntoMap, "Map", core),
    )
    val scopes = ScopeLookupAnnotation(DaggerNames.Singleton, JsrNames.Scope)
    val function = IncludeFunctionOriginating(
        IncludeFunctionMapKey(
            DaggerNames.Keys,
            IncludeFunctionNamed(
                names,
                JsrNames.SpikeNamed,
                IncludeFunctionScoped(scopes, SpikeNames.Singleton, contributing),
            ),
        ),
    )
    val conversion = ModuleConversionFile(function, environment.codeGenerator)
    return SymbolProcessorConversion(SymbolSourceAnnotated(DaggerNames.Module), check, conversion, reporter)
}

private fun daggerComponents(
    qualifiers: QualifierLookup,
    parameters: ParameterConversion,
    reporter: ProblemReporter,
    environment: SymbolProcessorEnvironment,
): SymbolProcessor {
    val check = ConversionCheckMapped(
        DaggerComponent::of,
        ConversionCheckFirst(listOf(ComponentCheckBuilder(), ComponentCheckQualifier(qualifiers))),
    )
    val type = EntryPointTypeOriginating(
        EntryPointTypeFactory(parameters, EntryPointTypeSupertype(EntryPointTypeInterface())),
    )
    val conversion = EntryPointConversionFile(DaggerComponent::of, type, environment.codeGenerator)
    return SymbolProcessorConversion(SymbolSourceAnnotated(DaggerNames.Component), check, conversion, reporter)
}
