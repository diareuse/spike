package spike.compiler.jsr

import com.google.devtools.ksp.symbol.KSAnnotated
import spike.BindTarget
import spike.Include
import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionCheckFirst
import spike.compiler.conversion.ConversionCheckMapped
import spike.compiler.conversion.Injection
import spike.compiler.conversion.InjectionCheckConstructor
import spike.compiler.conversion.InjectionCheckGeneric
import spike.compiler.conversion.InjectionCheckQualifier
import spike.compiler.conversion.InjectionCheckVisibility
import spike.compiler.conversion.Order
import spike.compiler.conversion.QualifierLookup
import spike.compiler.conversion.ordered

@Include
fun jsrCheck(injections: Map<Int, ConversionCheck<Injection>>): ConversionCheck<KSAnnotated> = ConversionCheckFirst(
    listOf(
        InjectionCheckConstructor(
            "SPIKE-JSR-FIELD-INJECTION",
            "Spike injects only constructors; move this dependency into the @Inject constructor.",
        ),
        ConversionCheckMapped(Injection::of, ConversionCheckFirst(injections.ordered())),
    ),
)

@Include(bindTo = BindTarget.Map)
@Order(10)
fun jsrSingleConstructor(): ConversionCheck<Injection> = InjectionCheckSingleConstructor(JsrNames.Inject)

@Include(bindTo = BindTarget.Map)
@Order(20)
fun jsrVisibility(): ConversionCheck<Injection> = InjectionCheckVisibility("SPIKE-JSR-VISIBILITY")

@Include(bindTo = BindTarget.Map)
@Order(30)
fun jsrGeneric(): ConversionCheck<Injection> = InjectionCheckGeneric("SPIKE-JSR-GENERIC")

@Include(bindTo = BindTarget.Map)
@Order(40)
fun jsrQualifier(qualifiers: QualifierLookup): ConversionCheck<Injection> = InjectionCheckQualifier(
    qualifiers,
    "SPIKE-JSR-QUALIFIER",
    "Only @Named is converted; annotate this qualifier with @spike.Qualifier instead of a JSR-330 one.",
)
