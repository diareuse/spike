package spike.compiler.metro

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.SymbolProcessor
import spike.BindTarget
import spike.Include
import spike.compiler.conversion.CheckParameterType
import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionCheckFirst
import spike.compiler.conversion.ConversionCheckMapped
import spike.compiler.conversion.IncludeConversionFile
import spike.compiler.conversion.IncludeFunctionConstructor
import spike.compiler.conversion.IncludeFunctionDecorator
import spike.compiler.conversion.IncludeFunctionInternal
import spike.compiler.conversion.Injection
import spike.compiler.conversion.InjectionCheckConstructor
import spike.compiler.conversion.InjectionCheckGeneric
import spike.compiler.conversion.InjectionCheckQualifier
import spike.compiler.conversion.InjectionCheckVisibility
import spike.compiler.conversion.NameLookup
import spike.compiler.conversion.Order
import spike.compiler.conversion.ParameterConversion
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
fun metroInjections(
    checks: Map<Int, ConversionCheck<Injection>>,
    decorators: Map<Int, IncludeFunctionDecorator<Injection>>,
    parameters: ParameterConversion,
    reporter: ProblemReporter,
    codeGenerator: CodeGenerator,
): SymbolProcessor {
    val check = ConversionCheckFirst(
        listOf(
            InjectionCheckConstructor(UNSUPPORTED_CODE, UNSUPPORTED_MESSAGE),
            ConversionCheckMapped(Injection::of, ConversionCheckFirst(checks.ordered())),
        ),
    )
    val function = decorators.applyTo(IncludeFunctionConstructor(SpikeNames.Include, parameters))
    val conversion = IncludeConversionFile(Injection::of, function, codeGenerator)
    return SymbolProcessorConversion(SymbolSourceAnnotated(MetroNames.Injected), check, conversion, reporter)
}

@Include(bindTo = BindTarget.Map)
@Order(10)
fun metroVisibility(): ConversionCheck<Injection> = InjectionCheckVisibility("SPIKE-METRO-VISIBILITY")

@Include(bindTo = BindTarget.Map)
@Order(20)
fun metroGeneric(): ConversionCheck<Injection> = InjectionCheckGeneric("SPIKE-METRO-GENERIC")

@Include(bindTo = BindTarget.Map)
@Order(30)
fun metroInjectionQualifier(qualifiers: QualifierLookup): ConversionCheck<Injection> =
    InjectionCheckQualifier(qualifiers, QUALIFIER_CODE, QUALIFIER_MESSAGE)

@Include(bindTo = BindTarget.Map)
@Order(40)
fun metroInjectionProvider(): ConversionCheck<Injection> =
    CheckParameterType({ it.constructor.parameters }, MetroNames.Provider, PROVIDER_CODE, PROVIDER_MESSAGE)
