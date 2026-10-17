package spike.compiler.kotlininject

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.SymbolProcessor
import spike.BindTarget
import spike.Include
import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionCheckFirst
import spike.compiler.conversion.ConversionCheckMapped
import spike.compiler.conversion.IncludeConversionFile
import spike.compiler.conversion.IncludeFunctionConstructor
import spike.compiler.conversion.IncludeFunctionDecorator
import spike.compiler.conversion.IncludeFunctionInternal
import spike.compiler.conversion.IncludeFunctionOriginating
import spike.compiler.conversion.IncludeFunctionScoped
import spike.compiler.conversion.Injection
import spike.compiler.conversion.InjectionCheckConstructor
import spike.compiler.conversion.InjectionCheckGeneric
import spike.compiler.conversion.InjectionCheckQualifier
import spike.compiler.conversion.InjectionCheckVisibility
import spike.compiler.conversion.Order
import spike.compiler.conversion.ParameterConversionDirect
import spike.compiler.conversion.ProblemReporter
import spike.compiler.conversion.QualifierLookup
import spike.compiler.conversion.ScopeLookup
import spike.compiler.conversion.SpikeNames
import spike.compiler.conversion.SymbolProcessorConversion
import spike.compiler.conversion.SymbolSourceAnnotated
import spike.compiler.conversion.applyTo
import spike.compiler.conversion.ordered

@Include(bindTo = BindTarget.Map)
@Order(10)
fun kotlinInjectInjections(
    checks: Map<Int, ConversionCheck<Injection>>,
    decorators: Map<Int, IncludeFunctionDecorator<Injection>>,
    reporter: ProblemReporter,
    codeGenerator: CodeGenerator,
): SymbolProcessor {
    val check = ConversionCheckFirst(
        listOf(
            InjectionCheckConstructor("SPIKE-KI-UNSUPPORTED", "This @Inject declaration cannot be converted."),
            ConversionCheckMapped(Injection::of, ConversionCheckFirst(checks.ordered())),
        ),
    )
    val function = decorators.applyTo(IncludeFunctionConstructor(SpikeNames.Include, ParameterConversionDirect()))
    val conversion = IncludeConversionFile(Injection::of, function, codeGenerator)
    return SymbolProcessorConversion(SymbolSourceAnnotated(KotlinInjectNames.Inject), check, conversion, reporter)
}

@Include(bindTo = BindTarget.Map)
@Order(10)
fun kotlinInjectVisibility(): ConversionCheck<Injection> = InjectionCheckVisibility("SPIKE-KI-VISIBILITY")

@Include(bindTo = BindTarget.Map)
@Order(20)
fun kotlinInjectGeneric(): ConversionCheck<Injection> = InjectionCheckGeneric("SPIKE-KI-GENERIC")

@Include(bindTo = BindTarget.Map)
@Order(30)
fun kotlinInjectInjectionQualifier(qualifiers: QualifierLookup): ConversionCheck<Injection> =
    InjectionCheckQualifier(qualifiers, QUALIFIER_CODE, QUALIFIER_MESSAGE)

@Include(bindTo = BindTarget.Map)
@Order(10)
fun kotlinInjectInternal(): IncludeFunctionDecorator<Injection> =
    IncludeFunctionDecorator { IncludeFunctionInternal(it) }

@Include(bindTo = BindTarget.Map)
@Order(20)
fun kotlinInjectInjectionScoped(scopes: ScopeLookup): IncludeFunctionDecorator<Injection> =
    IncludeFunctionDecorator { IncludeFunctionScoped(scopes, SpikeNames.Singleton, it) }

@Include(bindTo = BindTarget.Map)
@Order(30)
fun kotlinInjectInjectionOriginating(): IncludeFunctionDecorator<Injection> =
    IncludeFunctionDecorator { IncludeFunctionOriginating(it) }
