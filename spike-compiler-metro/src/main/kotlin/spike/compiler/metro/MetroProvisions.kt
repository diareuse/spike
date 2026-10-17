package spike.compiler.metro

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.SymbolProcessor
import spike.BindTarget
import spike.Include
import spike.compiler.conversion.CheckAnnotated
import spike.compiler.conversion.CheckParameterType
import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionCheckFirst
import spike.compiler.conversion.ConversionCheckMapped
import spike.compiler.conversion.IncludeConversionFile
import spike.compiler.conversion.IncludeFunctionDecorator
import spike.compiler.conversion.IncludeFunctionProvision
import spike.compiler.conversion.NameLookup
import spike.compiler.conversion.Order
import spike.compiler.conversion.ParameterConversion
import spike.compiler.conversion.ProblemReporter
import spike.compiler.conversion.Provision
import spike.compiler.conversion.ProvisionCallDecorator
import spike.compiler.conversion.ProvisionCallInterface
import spike.compiler.conversion.ProvisionCallObject
import spike.compiler.conversion.ProvisionCallReceiver
import spike.compiler.conversion.ProvisionCallTopLevel
import spike.compiler.conversion.ProvisionCheckParent
import spike.compiler.conversion.ProvisionCheckQualifier
import spike.compiler.conversion.ProvisionCheckStateless
import spike.compiler.conversion.QualifierLookup
import spike.compiler.conversion.ScopeLookup
import spike.compiler.conversion.SpikeNames
import spike.compiler.conversion.SymbolProcessorConversion
import spike.compiler.conversion.SymbolSourceAnnotated
import spike.compiler.conversion.SymbolSourceFiltered
import spike.compiler.conversion.applyTo
import spike.compiler.conversion.ordered

@Include(bindTo = BindTarget.Map)
@Order(20)
fun metroProvisions(
    checks: Map<Int, ConversionCheck<Provision>>,
    calls: Map<Int, ProvisionCallDecorator>,
    decorators: Map<Int, IncludeFunctionDecorator<Provision>>,
    parameters: ParameterConversion,
    reporter: ProblemReporter,
    codeGenerator: CodeGenerator,
): SymbolProcessor {
    val provisionOnly = listOf(
        ProvisionCheckParent(
            MetroNames.DependencyGraph,
            "SPIKE-METRO-GRAPH-PROVIDES",
            "A @Provides in a graph body needs the graph instance; move it into the graph's companion object.",
        ),
        ProvisionCheckStateless(
            "SPIKE-METRO-STATEFUL",
            "This @Provides belongs to a type with abstract members or state; move it into an object.",
        ),
    )
    val check = ConversionCheckMapped(Provision::of, ConversionCheckFirst(checks.ordered() + provisionOnly))
    val call = calls.applyTo(ProvisionCallTopLevel())
    val core = IncludeFunctionProvision(SpikeNames.Include, "provides", parameters, call)
    val conversion = IncludeConversionFile(Provision::of, decorators.applyTo(core), codeGenerator)
    val source = SymbolSourceFiltered(SymbolSourceAnnotated(MetroNames.Provides)) { Provision.of(it) != null }
    return SymbolProcessorConversion(source, check, conversion, reporter)
}

@Include(bindTo = BindTarget.Map)
@Order(30)
fun metroBinds(
    checks: Map<Int, ConversionCheck<Provision>>,
    decorators: Map<Int, IncludeFunctionDecorator<Provision>>,
    parameters: ParameterConversion,
    reporter: ProblemReporter,
    codeGenerator: CodeGenerator,
): SymbolProcessor {
    val source = SymbolSourceFiltered(SymbolSourceAnnotated(MetroNames.Binds)) { symbol ->
        Provision.of(symbol)?.receiver != null
    }
    val check = ConversionCheckMapped(Provision::of, ConversionCheckFirst(checks.ordered()))
    val core = IncludeFunctionProvision(SpikeNames.Include, "binds", parameters, ProvisionCallReceiver())
    val conversion = IncludeConversionFile(Provision::of, decorators.applyTo(core), codeGenerator)
    return SymbolProcessorConversion(source, check, conversion, reporter)
}

@Include(bindTo = BindTarget.Map)
@Order(10)
fun metroProvisionQualifier(qualifiers: QualifierLookup): ConversionCheck<Provision> =
    ProvisionCheckQualifier(qualifiers, QUALIFIER_CODE, QUALIFIER_MESSAGE)

@Include(bindTo = BindTarget.Map)
@Order(20)
fun metroOptional(): ConversionCheck<Provision> = CheckAnnotated(
    MetroNames.Unconvertible,
    "SPIKE-METRO-OPTIONAL",
    "@OptionalBinding and @DefaultBinding are not converted; Spike passes null for an unbound nullable type.",
)

@Include(bindTo = BindTarget.Map)
@Order(30)
fun metroProvisionProvider(): ConversionCheck<Provision> =
    CheckParameterType(Provision::parameters, MetroNames.Provider, PROVIDER_CODE, PROVIDER_MESSAGE)

@Include(bindTo = BindTarget.Map)
@Order(10)
fun metroInterfaceCall(): ProvisionCallDecorator = ProvisionCallDecorator { ProvisionCallInterface(it) }

@Include(bindTo = BindTarget.Map)
@Order(20)
fun metroObjectCall(): ProvisionCallDecorator = ProvisionCallDecorator { ProvisionCallObject(it) }
