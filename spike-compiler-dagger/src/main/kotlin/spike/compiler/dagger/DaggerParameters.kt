package spike.compiler.dagger

import spike.BindTarget
import spike.Include
import spike.compiler.conversion.NameLookup
import spike.compiler.conversion.Order
import spike.compiler.conversion.ParameterConversion
import spike.compiler.conversion.ParameterConversionDecorator
import spike.compiler.conversion.ParameterConversionDirect
import spike.compiler.conversion.ParameterConversionNamed
import spike.compiler.conversion.ParameterConversionWrapped
import spike.compiler.conversion.SpikeNames
import spike.compiler.conversion.applyTo
import spike.compiler.jsr.JsrNames

@Include
fun daggerParameters(decorators: Map<Int, ParameterConversionDecorator>): ParameterConversion =
    decorators.applyTo(ParameterConversionDirect())

@Include(bindTo = BindTarget.Map)
@Order(10)
fun daggerLazyParameters(): ParameterConversionDecorator = ParameterConversionDecorator {
    ParameterConversionWrapped(DaggerNames.Lazy, DaggerNames.KotlinLazy, "value", it)
}

@Include(bindTo = BindTarget.Map)
@Order(20)
fun daggerProviderParameters(): ParameterConversionDecorator = ParameterConversionDecorator {
    ParameterConversionWrapped(JsrNames.Provider, SpikeNames.Provider, "get()", it)
}

@Include(bindTo = BindTarget.Map)
@Order(30)
fun daggerNamedParameters(names: NameLookup): ParameterConversionDecorator = ParameterConversionDecorator {
    ParameterConversionNamed(names, JsrNames.SpikeNamed, it)
}
