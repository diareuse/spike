package spike.compiler.dagger

import com.google.devtools.ksp.processing.SymbolProcessor
import spike.BindTarget
import spike.Include
import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionCheckFirst
import spike.compiler.conversion.ConversionCheckMapped
import spike.compiler.conversion.IncludeFunction
import spike.compiler.conversion.IncludeFunctionBindTo
import spike.compiler.conversion.IncludeFunctionDecorator
import spike.compiler.conversion.IncludeFunctionMapKey
import spike.compiler.conversion.IncludeFunctionNamed
import spike.compiler.conversion.IncludeFunctionOriginating
import spike.compiler.conversion.IncludeFunctionScoped
import spike.compiler.conversion.NameLookup
import spike.compiler.conversion.Order
import spike.compiler.conversion.ProblemReporter
import spike.compiler.conversion.ScopeLookup
import spike.compiler.conversion.SpikeNames
import spike.compiler.conversion.SymbolProcessorConversion
import spike.compiler.conversion.SymbolSourceAnnotated
import spike.compiler.conversion.applyTo
import spike.compiler.conversion.ordered
import spike.compiler.jsr.JsrNames

@Include(bindTo = BindTarget.Map)
@Order(10)
fun daggerModules(
    checks: Map<Int, ConversionCheck<DaggerModule>>,
    conversion: ModuleConversionFile,
    reporter: ProblemReporter,
): SymbolProcessor = SymbolProcessorConversion(
    SymbolSourceAnnotated(DaggerNames.Module),
    ConversionCheckMapped(DaggerModule::of, ConversionCheckFirst(checks.ordered())),
    conversion,
    reporter,
)

@Include
fun daggerBindingCheck(checks: Map<Int, ConversionCheck<DaggerBinding>>): ConversionCheck<DaggerBinding> =
    ConversionCheckFirst(checks.ordered())

@Include
fun daggerBindingCall(core: BindingCallStatic, decorators: Map<Int, BindingCallDecorator>): BindingCall =
    decorators.ordered().fold(core as BindingCall) { inner, decorator -> decorator.decorate(inner) }

@Include
fun daggerBindingFunction(
    core: IncludeFunctionBinding,
    decorators: Map<Int, IncludeFunctionDecorator<DaggerBinding>>,
): IncludeFunction<DaggerBinding> = decorators.applyTo(core)

@Include(bindTo = BindTarget.Map)
@Order(10)
fun daggerIntoMap(): IncludeFunctionDecorator<DaggerBinding> = IncludeFunctionDecorator {
    IncludeFunctionBindTo(DaggerNames.IntoMap, "Map", it)
}

@Include(bindTo = BindTarget.Map)
@Order(20)
fun daggerIntoSet(): IncludeFunctionDecorator<DaggerBinding> = IncludeFunctionDecorator {
    IncludeFunctionBindTo(DaggerNames.IntoSet, "Set", it)
}

@Include(bindTo = BindTarget.Map)
@Order(30)
fun daggerScoped(scopes: ScopeLookup): IncludeFunctionDecorator<DaggerBinding> = IncludeFunctionDecorator {
    IncludeFunctionScoped(scopes, SpikeNames.Singleton, it)
}

@Include(bindTo = BindTarget.Map)
@Order(40)
fun daggerNamed(names: NameLookup): IncludeFunctionDecorator<DaggerBinding> = IncludeFunctionDecorator {
    IncludeFunctionNamed(names, JsrNames.SpikeNamed, it)
}

@Include(bindTo = BindTarget.Map)
@Order(50)
fun daggerMapKey(): IncludeFunctionDecorator<DaggerBinding> = IncludeFunctionDecorator {
    IncludeFunctionMapKey(DaggerNames.Keys, it)
}

@Include(bindTo = BindTarget.Map)
@Order(60)
fun daggerOriginating(): IncludeFunctionDecorator<DaggerBinding> = IncludeFunctionDecorator {
    IncludeFunctionOriginating(it)
}
