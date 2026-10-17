package spike.compiler.kotlininject

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import spike.EntryPoint
import spike.Include
import spike.compiler.conversion.AnnotationNames
import spike.compiler.conversion.CheckAnnotated
import spike.compiler.conversion.ConversionCheckFirst
import spike.compiler.conversion.ConversionCheckMapped
import spike.compiler.conversion.EntryPointConversionFile
import spike.compiler.conversion.EntryPointTypeFunctions
import spike.compiler.conversion.EntryPointTypeInterface
import spike.compiler.conversion.EntryPointTypeOriginating
import spike.compiler.conversion.EntryPointTypeProperties
import spike.compiler.conversion.IncludeConversionFile
import spike.compiler.conversion.IncludeFunctionBindTo
import spike.compiler.conversion.IncludeFunctionConstructor
import spike.compiler.conversion.IncludeFunctionInternal
import spike.compiler.conversion.IncludeFunctionOriginating
import spike.compiler.conversion.IncludeFunctionProvision
import spike.compiler.conversion.IncludeFunctionScoped
import spike.compiler.conversion.Injection
import spike.compiler.conversion.InjectionCheckConstructor
import spike.compiler.conversion.InjectionCheckGeneric
import spike.compiler.conversion.InjectionCheckQualifier
import spike.compiler.conversion.InjectionCheckVisibility
import spike.compiler.conversion.ParameterConversionDirect
import spike.compiler.conversion.ProblemReporter
import spike.compiler.conversion.ProblemReporterLogger
import spike.compiler.conversion.Provision
import spike.compiler.conversion.ProvisionCallInterface
import spike.compiler.conversion.ProvisionCallObject
import spike.compiler.conversion.ProvisionCallTopLevel
import spike.compiler.conversion.ProvisionCheckParent
import spike.compiler.conversion.ProvisionCheckQualifier
import spike.compiler.conversion.ProvisionCheckStateless
import spike.compiler.conversion.QualifierLookup
import spike.compiler.conversion.QualifierLookupMeta
import spike.compiler.conversion.ScopeLookup
import spike.compiler.conversion.ScopeLookupAnnotation
import spike.compiler.conversion.SpikeNames
import spike.compiler.conversion.SymbolProcessorAll
import spike.compiler.conversion.SymbolProcessorConversion
import spike.compiler.conversion.SymbolProcessorOnce
import spike.compiler.conversion.SymbolSourceAnnotated
import spike.compiler.conversion.SymbolSourceFiltered

@EntryPoint
interface KotlinInjectConversion {
    val processor: SymbolProcessor

    @EntryPoint.Factory
    interface Factory {
        fun create(environment: SymbolProcessorEnvironment): KotlinInjectConversion
    }

    companion object
}

private const val QUALIFIER_CODE = "SPIKE-KI-QUALIFIER"
private const val QUALIFIER_MESSAGE =
    "kotlin-inject qualifiers are not converted; annotate the qualifier with @spike.Qualifier instead."

@Include
fun kotlinInjectProcessor(
    qualifiers: QualifierLookup,
    scopes: ScopeLookup,
    reporter: ProblemReporter,
    environment: SymbolProcessorEnvironment,
): SymbolProcessor = SymbolProcessorOnce(
    SymbolProcessorAll(
        listOf(
            kotlinInjectInjections(qualifiers, scopes, reporter, environment),
            kotlinInjectProvisions(qualifiers, scopes, reporter, environment),
            kotlinInjectComponents(reporter, environment),
        ),
    ),
)

@Include
fun kotlinInjectReporter(environment: SymbolProcessorEnvironment): ProblemReporter =
    ProblemReporterLogger(environment.logger)

@Include
fun kotlinInjectQualifiers(): QualifierLookup =
    QualifierLookupMeta(KotlinInjectNames.Qualifier, converted = AnnotationNames())

@Include
fun kotlinInjectScopes(): ScopeLookup = ScopeLookupAnnotation(singleton = AnnotationNames(), KotlinInjectNames.Scope)

private fun kotlinInjectInjections(
    qualifiers: QualifierLookup,
    scopes: ScopeLookup,
    reporter: ProblemReporter,
    environment: SymbolProcessorEnvironment,
): SymbolProcessor {
    val check = ConversionCheckFirst(
        listOf(
            InjectionCheckConstructor("SPIKE-KI-UNSUPPORTED", "This @Inject declaration cannot be converted."),
            ConversionCheckMapped(
                Injection::of,
                ConversionCheckFirst(
                    listOf(
                        InjectionCheckVisibility("SPIKE-KI-VISIBILITY"),
                        InjectionCheckGeneric("SPIKE-KI-GENERIC"),
                        InjectionCheckQualifier(qualifiers, QUALIFIER_CODE, QUALIFIER_MESSAGE),
                    ),
                ),
            ),
        ),
    )
    val core = IncludeFunctionConstructor(SpikeNames.Include, ParameterConversionDirect())
    val function = IncludeFunctionOriginating(
        IncludeFunctionScoped(scopes, SpikeNames.Singleton, IncludeFunctionInternal(core)),
    )
    val conversion = IncludeConversionFile(Injection::of, function, environment.codeGenerator)
    return SymbolProcessorConversion(SymbolSourceAnnotated(KotlinInjectNames.Inject), check, conversion, reporter)
}

private fun kotlinInjectProvisions(
    qualifiers: QualifierLookup,
    scopes: ScopeLookup,
    reporter: ProblemReporter,
    environment: SymbolProcessorEnvironment,
): SymbolProcessor {
    val source = SymbolSourceFiltered(SymbolSourceAnnotated(KotlinInjectNames.Provides)) { symbol ->
        Provision.of(symbol) != null && !KotlinInjectComponent.isInput(symbol)
    }
    val check = ConversionCheckMapped(
        Provision::of,
        ConversionCheckFirst(
            listOf(
                ProvisionCheckParent(
                    KotlinInjectNames.Component,
                    "SPIKE-KI-COMPONENT-PROVIDES",
                    "A @Provides inside a component class needs the component instance; " +
                        "move it into an interface or object.",
                ),
                CheckAnnotated(
                    KotlinInjectNames.IntoMap,
                    "SPIKE-KI-MAP",
                    "@IntoMap entries are Pairs built at runtime; Spike needs a @spike.Key annotation, " +
                        "so add an @Include instead.",
                ),
                ProvisionCheckQualifier(qualifiers, QUALIFIER_CODE, QUALIFIER_MESSAGE),
                ProvisionCheckStateless(
                    "SPIKE-KI-STATEFUL",
                    "This @Provides belongs to a type with abstract members or state; " +
                        "move it into an object or a stateless interface.",
                ),
            ),
        ),
    )
    val core = IncludeFunctionProvision(
        SpikeNames.Include,
        "provides",
        ParameterConversionDirect(),
        ProvisionCallObject(ProvisionCallInterface(ProvisionCallTopLevel())),
    )
    val function = IncludeFunctionOriginating(
        IncludeFunctionScoped(
            scopes,
            SpikeNames.Singleton,
            IncludeFunctionBindTo(KotlinInjectNames.IntoSet, "Set", core),
        ),
    )
    val conversion = IncludeConversionFile(Provision::of, function, environment.codeGenerator)
    return SymbolProcessorConversion(source, check, conversion, reporter)
}

private fun kotlinInjectComponents(
    reporter: ProblemReporter,
    environment: SymbolProcessorEnvironment,
): SymbolProcessor {
    val check = ConversionCheckMapped(KotlinInjectComponent::of, ComponentCheckParent())
    val type = EntryPointTypeOriginating(
        EntryPointTypeInputs(
            EntryPointTypeFunctions(
                AnnotationNames(),
                EntryPointTypeProperties(AnnotationNames(), EntryPointTypeInterface()),
            ),
        ),
    )
    val conversion = EntryPointConversionFile(KotlinInjectComponent::of, type, environment.codeGenerator)
    val source = SymbolSourceFiltered(SymbolSourceAnnotated(KotlinInjectNames.Component)) { symbol ->
        KotlinInjectComponent.of(symbol) != null
    }
    return SymbolProcessorConversion(source, check, conversion, reporter)
}
