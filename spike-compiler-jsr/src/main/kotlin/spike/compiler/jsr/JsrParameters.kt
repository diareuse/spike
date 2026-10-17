package spike.compiler.jsr

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

@Include
fun jsrParameters(decorators: Map<Int, ParameterConversionDecorator>): ParameterConversion =
    decorators.applyTo(ParameterConversionDirect())

@Include(bindTo = BindTarget.Map)
@Order(10)
fun jsrProviderParameters(): ParameterConversionDecorator = ParameterConversionDecorator {
    ParameterConversionWrapped(JsrNames.Provider, SpikeNames.Provider, "get()", it)
}

@Include(bindTo = BindTarget.Map)
@Order(20)
fun jsrNamedParameters(names: NameLookup): ParameterConversionDecorator = ParameterConversionDecorator {
    ParameterConversionNamed(names, JsrNames.SpikeNamed, it)
}
