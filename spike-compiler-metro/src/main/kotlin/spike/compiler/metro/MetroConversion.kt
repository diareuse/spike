package spike.compiler.metro

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.EntryPoint
import spike.Include
import spike.compiler.conversion.AnnotationNames
import spike.compiler.conversion.CheckAnnotated
import spike.compiler.conversion.CheckParameterType
import spike.compiler.conversion.Conversion
import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionCheckFirst
import spike.compiler.conversion.ConversionCheckMapped
import spike.compiler.conversion.Declared
import spike.compiler.conversion.EntryPointConversionFile
import spike.compiler.conversion.EntryPointTypeFunctions
import spike.compiler.conversion.EntryPointTypeInterface
import spike.compiler.conversion.EntryPointTypeOriginating
import spike.compiler.conversion.EntryPointTypeProperties
import spike.compiler.conversion.IncludeConversionFile
import spike.compiler.conversion.IncludeFunction
import spike.compiler.conversion.IncludeFunctionBindTo
import spike.compiler.conversion.IncludeFunctionConstructor
import spike.compiler.conversion.IncludeFunctionInternal
import spike.compiler.conversion.IncludeFunctionMapKey
import spike.compiler.conversion.IncludeFunctionNamed
import spike.compiler.conversion.IncludeFunctionOriginating
import spike.compiler.conversion.IncludeFunctionProvision
import spike.compiler.conversion.IncludeFunctionScoped
import spike.compiler.conversion.Injection
import spike.compiler.conversion.InjectionCheckConstructor
import spike.compiler.conversion.InjectionCheckGeneric
import spike.compiler.conversion.InjectionCheckQualifier
import spike.compiler.conversion.InjectionCheckVisibility
import spike.compiler.conversion.NameLookup
import spike.compiler.conversion.NameLookupAnnotation
import spike.compiler.conversion.ParameterConversion
import spike.compiler.conversion.ParameterConversionDirect
import spike.compiler.conversion.ParameterConversionNamed
import spike.compiler.conversion.ProblemReporter
import spike.compiler.conversion.ProblemReporterLogger
import spike.compiler.conversion.Provision
import spike.compiler.conversion.ProvisionCallInterface
import spike.compiler.conversion.ProvisionCallObject
import spike.compiler.conversion.ProvisionCallReceiver
import spike.compiler.conversion.ProvisionCallTopLevel
import spike.compiler.conversion.ProvisionCheckParent
import spike.compiler.conversion.ProvisionCheckQualifier
import spike.compiler.conversion.ProvisionCheckStateless
import spike.compiler.conversion.QualifierLookup
import spike.compiler.conversion.QualifierLookupMeta
import spike.compiler.conversion.ScopeLookupAnnotation
import spike.compiler.conversion.SpikeNames
import spike.compiler.conversion.SymbolProcessorAll
import spike.compiler.conversion.SymbolProcessorConversion
import spike.compiler.conversion.SymbolProcessorOnce
import spike.compiler.conversion.SymbolProcessorRejecting
import spike.compiler.conversion.SymbolSource
import spike.compiler.conversion.SymbolSourceAnnotated
import spike.compiler.conversion.SymbolSourceFiltered

@EntryPoint
interface MetroConversion {
    val processor: SymbolProcessor

    @EntryPoint.Factory
    interface Factory {
        fun create(environment: SymbolProcessorEnvironment): MetroConversion
    }

    companion object
}

private const val UNSUPPORTED_CODE = "SPIKE-METRO-UNSUPPORTED"
private const val UNSUPPORTED_MESSAGE = "This Metro declaration cannot be converted."
private const val QUALIFIER_CODE = "SPIKE-METRO-QUALIFIER"
private const val QUALIFIER_MESSAGE = "Only @Named is converted; annotate this qualifier with @spike.Qualifier instead."
private const val PROVIDER_CODE = "SPIKE-METRO-PROVIDER"
private const val PROVIDER_MESSAGE = "Metro's Provider is not converted; request spike.Provider or Lazy instead."

@Include
fun metroProcessor(
    names: NameLookup,
    qualifiers: QualifierLookup,
    parameters: ParameterConversion,
    reporter: ProblemReporter,
    environment: SymbolProcessorEnvironment,
): SymbolProcessor {
    val declarations = MetroDeclarations(names, qualifiers, parameters, reporter, environment)
    return SymbolProcessorOnce(
        SymbolProcessorAll(
            listOf(
                declarations.injections(),
                declarations.provisions(),
                declarations.binds(),
                declarations.contributions(),
                declarations.assistedFactories(),
                declarations.graphs(),
                SymbolProcessorRejecting(
                    SymbolSourceAnnotated(MetroNames.GraphExtension),
                    "SPIKE-METRO-EXTENSION",
                    "Graph extensions are not converted; Spike has no graph hierarchy, so use a separate entry point.",
                    reporter,
                ),
            ),
        ),
    )
}

@Include
fun metroReporter(environment: SymbolProcessorEnvironment): ProblemReporter = ProblemReporterLogger(environment.logger)

@Include
fun metroNames(): NameLookup = NameLookupAnnotation(MetroNames.Named)

@Include
fun metroQualifiers(): QualifierLookup = QualifierLookupMeta(MetroNames.Qualifier, converted = MetroNames.Named)

@Include
fun metroParameters(names: NameLookup): ParameterConversion =
    ParameterConversionNamed(names, MetroNames.SpikeNamed, ParameterConversionDirect())

private class MetroDeclarations(
    private val names: NameLookup,
    private val qualifiers: QualifierLookup,
    private val parameters: ParameterConversion,
    private val reporter: ProblemReporter,
    private val environment: SymbolProcessorEnvironment,
) {
    fun injections(): SymbolProcessor {
        val check = ConversionCheckFirst(
            listOf(
                InjectionCheckConstructor(UNSUPPORTED_CODE, UNSUPPORTED_MESSAGE),
                ConversionCheckMapped(
                    Injection::of,
                    ConversionCheckFirst(
                        listOf(
                            InjectionCheckVisibility("SPIKE-METRO-VISIBILITY"),
                            InjectionCheckGeneric("SPIKE-METRO-GENERIC"),
                            InjectionCheckQualifier(qualifiers, QUALIFIER_CODE, QUALIFIER_MESSAGE),
                            CheckParameterType<Injection>(
                                { it.constructor.parameters },
                                MetroNames.Provider,
                                PROVIDER_CODE,
                                PROVIDER_MESSAGE,
                            ),
                        ),
                    ),
                ),
            ),
        )
        val function = annotated(IncludeFunctionInternal(IncludeFunctionConstructor(SpikeNames.Include, parameters)))
        val conversion = IncludeConversionFile(Injection::of, function, codeGenerator)
        return processor(SymbolSourceAnnotated(MetroNames.Injected), check, conversion)
    }

    fun provisions(): SymbolProcessor {
        val check = ConversionCheckMapped(
            Provision::of,
            ConversionCheckFirst(
                declarationChecks() + listOf(
                    ProvisionCheckParent(
                        MetroNames.DependencyGraph,
                        "SPIKE-METRO-GRAPH-PROVIDES",
                        "A @Provides in a graph body needs the graph instance; " +
                            "move it into the graph's companion object.",
                    ),
                    ProvisionCheckStateless(
                        "SPIKE-METRO-STATEFUL",
                        "This @Provides belongs to a type with abstract members or state; move it into an object.",
                    ),
                ),
            ),
        )
        val call = ProvisionCallObject(ProvisionCallInterface(ProvisionCallTopLevel()))
        val function = annotated(IncludeFunctionProvision(SpikeNames.Include, "provides", parameters, call))
        val conversion = IncludeConversionFile(Provision::of, function, codeGenerator)
        val source = SymbolSourceFiltered(SymbolSourceAnnotated(MetroNames.Provides)) { Provision.of(it) != null }
        return processor(source, check, conversion)
    }

    fun binds(): SymbolProcessor {
        val source = SymbolSourceFiltered(SymbolSourceAnnotated(MetroNames.Binds)) { symbol ->
            Provision.of(symbol)?.receiver != null
        }
        val check = ConversionCheckMapped(Provision::of, ConversionCheckFirst(declarationChecks()))
        val core = IncludeFunctionProvision(SpikeNames.Include, "binds", parameters, ProvisionCallReceiver())
        val conversion = IncludeConversionFile(Provision::of, annotated(core), codeGenerator)
        return processor(source, check, conversion)
    }

    fun contributions(): SymbolProcessor {
        val check = ConversionCheckMapped(Contribution::of, ContributionCheckSingle())
        val function = IncludeFunctionOriginating(
            IncludeFunctionBindTo(
                MetroNames.ContributesIntoSet,
                "Set",
                IncludeFunctionContribution(SpikeNames.Include),
            ),
        )
        val conversion = IncludeConversionFile(Contribution::of, function, codeGenerator)
        return processor(classes(MetroNames.Contributes), check, conversion)
    }

    fun assistedFactories(): SymbolProcessor {
        val check = ConversionCheckMapped(
            MetroAssistedFactory::of,
            AssistedFactoryCheckCreate(UNSUPPORTED_CODE, UNSUPPORTED_MESSAGE),
        )
        val function = IncludeFunctionOriginating(IncludeFunctionAssistedFactory(SpikeNames.Include, parameters))
        val conversion = IncludeConversionFile(MetroAssistedFactory::of, function, codeGenerator)
        return processor(classes(MetroNames.AssistedFactory), check, conversion)
    }

    fun graphs(): SymbolProcessor {
        val check = ConversionCheckMapped(
            MetroGraph::of,
            ConversionCheckFirst(
                listOf(GraphCheckIncludes(), GraphCheckQualifier(qualifiers, QUALIFIER_CODE, QUALIFIER_MESSAGE)),
            ),
        )
        val members = EntryPointTypeFunctions(
            MetroNames.Binds,
            EntryPointTypeProperties(MetroNames.Binds, EntryPointTypeInterface()),
        )
        val type = EntryPointTypeOriginating(EntryPointTypeGraphFactory(parameters, members))
        val conversion = EntryPointConversionFile(MetroGraph::of, type, codeGenerator)
        return processor(classes(MetroNames.DependencyGraph), check, conversion)
    }

    private val codeGenerator get() = environment.codeGenerator

    private fun declarationChecks(): List<ConversionCheck<Provision>> = listOf(
        ProvisionCheckQualifier(qualifiers, QUALIFIER_CODE, QUALIFIER_MESSAGE),
        CheckAnnotated(
            MetroNames.Unconvertible,
            "SPIKE-METRO-OPTIONAL",
            "@OptionalBinding and @DefaultBinding are not converted; " +
                "Spike passes null for an unbound nullable type.",
        ),
        CheckParameterType(Provision::parameters, MetroNames.Provider, PROVIDER_CODE, PROVIDER_MESSAGE),
    )

    private fun <T : Declared> annotated(core: IncludeFunction<T>): IncludeFunction<T> = IncludeFunctionOriginating(
        IncludeFunctionMapKey(
            MetroNames.Keys,
            IncludeFunctionNamed(
                names,
                MetroNames.SpikeNamed,
                IncludeFunctionScoped(
                    ScopeLookupAnnotation(MetroNames.SingleIn, MetroNames.Scope),
                    SpikeNames.Singleton,
                    IncludeFunctionBindTo(
                        MetroNames.IntoSet,
                        "Set",
                        IncludeFunctionBindTo(MetroNames.IntoMap, "Map", core),
                    ),
                ),
            ),
        ),
    )

    private fun processor(
        source: SymbolSource,
        check: ConversionCheck<KSAnnotated>,
        conversion: Conversion<KSAnnotated>,
    ): SymbolProcessor = SymbolProcessorConversion(source, check, conversion, reporter)

    private fun classes(names: AnnotationNames): SymbolSource =
        SymbolSourceFiltered(SymbolSourceAnnotated(names)) { it is KSClassDeclaration }
}
