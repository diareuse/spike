package spike.compiler.kotlininject

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.SymbolProcessor
import spike.BindTarget
import spike.Include
import spike.compiler.conversion.CheckAnnotated
import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionCheckFirst
import spike.compiler.conversion.ConversionCheckMapped
import spike.compiler.conversion.IncludeConversionFile
import spike.compiler.conversion.IncludeFunctionBindTo
import spike.compiler.conversion.IncludeFunctionDecorator
import spike.compiler.conversion.IncludeFunctionOriginating
import spike.compiler.conversion.IncludeFunctionProvision
import spike.compiler.conversion.IncludeFunctionScoped
import spike.compiler.conversion.Order
import spike.compiler.conversion.ParameterConversionDirect
import spike.compiler.conversion.ProblemReporter
import spike.compiler.conversion.Provision
import spike.compiler.conversion.ProvisionCallDecorator
import spike.compiler.conversion.ProvisionCallInterface
import spike.compiler.conversion.ProvisionCallObject
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
fun kotlinInjectProvisions(
    checks: Map<Int, ConversionCheck<Provision>>,
    calls: Map<Int, ProvisionCallDecorator>,
    decorators: Map<Int, IncludeFunctionDecorator<Provision>>,
    reporter: ProblemReporter,
    codeGenerator: CodeGenerator,
): SymbolProcessor {
    val source = SymbolSourceFiltered(SymbolSourceAnnotated(KotlinInjectNames.Provides)) { symbol ->
        Provision.of(symbol) != null && !KotlinInjectComponent.isInput(symbol)
    }
    val check = ConversionCheckMapped(Provision::of, ConversionCheckFirst(checks.ordered()))
    val core = IncludeFunctionProvision(
        SpikeNames.Include,
        "provides",
        ParameterConversionDirect(),
        calls.applyTo(ProvisionCallTopLevel()),
    )
    val conversion = IncludeConversionFile(Provision::of, decorators.applyTo(core), codeGenerator)
    return SymbolProcessorConversion(source, check, conversion, reporter)
}

@Include(bindTo = BindTarget.Map)
@Order(10)
fun kotlinInjectComponentProvides(): ConversionCheck<Provision> = ProvisionCheckParent(
    KotlinInjectNames.Component,
    "SPIKE-KI-COMPONENT-PROVIDES",
    "A @Provides inside a component class needs the component instance; move it into an interface or object.",
)

@Include(bindTo = BindTarget.Map)
@Order(20)
fun kotlinInjectIntoMap(): ConversionCheck<Provision> = CheckAnnotated(
    KotlinInjectNames.IntoMap,
    "SPIKE-KI-MAP",
    "@IntoMap entries are Pairs built at runtime; Spike needs a @spike.Key annotation, so add an @Include instead.",
)

@Include(bindTo = BindTarget.Map)
@Order(30)
fun kotlinInjectProvisionQualifier(qualifiers: QualifierLookup): ConversionCheck<Provision> =
    ProvisionCheckQualifier(qualifiers, QUALIFIER_CODE, QUALIFIER_MESSAGE)

@Include(bindTo = BindTarget.Map)
@Order(40)
fun kotlinInjectStateless(): ConversionCheck<Provision> = ProvisionCheckStateless(
    "SPIKE-KI-STATEFUL",
    "This @Provides belongs to a type with abstract members or state; move it into an object or a stateless interface.",
)

@Include(bindTo = BindTarget.Map)
@Order(10)
fun kotlinInjectInterfaceCall(): ProvisionCallDecorator = ProvisionCallDecorator { ProvisionCallInterface(it) }

@Include(bindTo = BindTarget.Map)
@Order(20)
fun kotlinInjectObjectCall(): ProvisionCallDecorator = ProvisionCallDecorator { ProvisionCallObject(it) }

@Include(bindTo = BindTarget.Map)
@Order(10)
fun kotlinInjectIntoSet(): IncludeFunctionDecorator<Provision> = IncludeFunctionDecorator {
    IncludeFunctionBindTo(KotlinInjectNames.IntoSet, "Set", it)
}

@Include(bindTo = BindTarget.Map)
@Order(20)
fun kotlinInjectProvisionScoped(scopes: ScopeLookup): IncludeFunctionDecorator<Provision> =
    IncludeFunctionDecorator { IncludeFunctionScoped(scopes, SpikeNames.Singleton, it) }

@Include(bindTo = BindTarget.Map)
@Order(30)
fun kotlinInjectProvisionOriginating(): IncludeFunctionDecorator<Provision> =
    IncludeFunctionDecorator { IncludeFunctionOriginating(it) }
