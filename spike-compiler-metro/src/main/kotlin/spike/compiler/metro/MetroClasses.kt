package spike.compiler.metro

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.SymbolProcessor
import spike.BindTarget
import spike.Include
import spike.compiler.conversion.ConversionCheckFirst
import spike.compiler.conversion.ConversionCheckMapped
import spike.compiler.conversion.EntryPointConversionFile
import spike.compiler.conversion.EntryPointTypeDecorator
import spike.compiler.conversion.EntryPointTypeFunctions
import spike.compiler.conversion.EntryPointTypeInterface
import spike.compiler.conversion.EntryPointTypeOriginating
import spike.compiler.conversion.EntryPointTypeProperties
import spike.compiler.conversion.IncludeConversionFile
import spike.compiler.conversion.IncludeFunctionBindTo
import spike.compiler.conversion.IncludeFunctionOriginating
import spike.compiler.conversion.Order
import spike.compiler.conversion.ParameterConversion
import spike.compiler.conversion.ProblemReporter
import spike.compiler.conversion.QualifierLookup
import spike.compiler.conversion.SymbolProcessorConversion
import spike.compiler.conversion.applyTo
import spike.compiler.conversion.ordered

@Include(bindTo = BindTarget.Map)
@Order(40)
fun metroContributions(
    check: ContributionCheckSingle,
    core: IncludeFunctionContribution,
    reporter: ProblemReporter,
    codeGenerator: CodeGenerator,
): SymbolProcessor {
    val function = IncludeFunctionOriginating(IncludeFunctionBindTo(MetroNames.ContributesIntoSet, "Set", core))
    val conversion = IncludeConversionFile(Contribution::of, function, codeGenerator)
    return SymbolProcessorConversion(
        classesWith(MetroNames.Contributes),
        ConversionCheckMapped(Contribution::of, check),
        conversion,
        reporter,
    )
}

@Include(bindTo = BindTarget.Map)
@Order(50)
fun metroAssistedFactories(
    core: IncludeFunctionAssistedFactory,
    reporter: ProblemReporter,
    codeGenerator: CodeGenerator,
): SymbolProcessor = SymbolProcessorConversion(
    classesWith(MetroNames.AssistedFactory),
    ConversionCheckMapped(MetroAssistedFactory::of, AssistedFactoryCheckCreate(UNSUPPORTED_CODE, UNSUPPORTED_MESSAGE)),
    IncludeConversionFile(MetroAssistedFactory::of, IncludeFunctionOriginating(core), codeGenerator),
    reporter,
)

@Include(bindTo = BindTarget.Map)
@Order(60)
fun metroGraphs(
    includes: GraphCheckIncludes,
    qualifiers: QualifierLookup,
    decorators: Map<Int, EntryPointTypeDecorator<MetroGraph>>,
    reporter: ProblemReporter,
    codeGenerator: CodeGenerator,
): SymbolProcessor {
    val checks = listOf(includes, GraphCheckQualifier(qualifiers, QUALIFIER_CODE, QUALIFIER_MESSAGE))
    return SymbolProcessorConversion(
        classesWith(MetroNames.DependencyGraph),
        ConversionCheckMapped(MetroGraph::of, ConversionCheckFirst(checks)),
        EntryPointConversionFile(MetroGraph::of, decorators.applyTo(EntryPointTypeInterface()), codeGenerator),
        reporter,
    )
}

@Include(bindTo = BindTarget.Map)
@Order(10)
fun metroGraphProperties(): EntryPointTypeDecorator<MetroGraph> =
    EntryPointTypeDecorator { EntryPointTypeProperties(MetroNames.Binds, it) }

@Include(bindTo = BindTarget.Map)
@Order(20)
fun metroGraphFunctions(): EntryPointTypeDecorator<MetroGraph> =
    EntryPointTypeDecorator { EntryPointTypeFunctions(MetroNames.Binds, it) }

@Include(bindTo = BindTarget.Map)
@Order(30)
fun metroGraphFactory(parameters: ParameterConversion): EntryPointTypeDecorator<MetroGraph> =
    EntryPointTypeDecorator { EntryPointTypeGraphFactory(parameters, it) }

@Include(bindTo = BindTarget.Map)
@Order(40)
fun metroGraphOriginating(): EntryPointTypeDecorator<MetroGraph> =
    EntryPointTypeDecorator { EntryPointTypeOriginating(it) }
