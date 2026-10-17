package spike.compiler.jsr

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.symbol.KSAnnotated
import spike.BindTarget
import spike.Include
import spike.compiler.conversion.Conversion
import spike.compiler.conversion.IncludeConversionFile
import spike.compiler.conversion.IncludeFunctionConstructor
import spike.compiler.conversion.IncludeFunctionDecorator
import spike.compiler.conversion.IncludeFunctionInternal
import spike.compiler.conversion.IncludeFunctionNamed
import spike.compiler.conversion.IncludeFunctionOriginating
import spike.compiler.conversion.IncludeFunctionScoped
import spike.compiler.conversion.Injection
import spike.compiler.conversion.NameLookup
import spike.compiler.conversion.Order
import spike.compiler.conversion.ParameterConversion
import spike.compiler.conversion.ScopeLookup
import spike.compiler.conversion.SpikeNames
import spike.compiler.conversion.applyTo

@Include
fun jsrConversion(
    parameters: ParameterConversion,
    decorators: Map<Int, IncludeFunctionDecorator<Injection>>,
    codeGenerator: CodeGenerator,
): Conversion<KSAnnotated> = IncludeConversionFile(
    Injection::of,
    decorators.applyTo(IncludeFunctionConstructor(SpikeNames.Include, parameters)),
    codeGenerator,
)

@Include(bindTo = BindTarget.Map)
@Order(10)
fun jsrInternal(): IncludeFunctionDecorator<Injection> = IncludeFunctionDecorator { IncludeFunctionInternal(it) }

@Include(bindTo = BindTarget.Map)
@Order(20)
fun jsrScoped(scopes: ScopeLookup): IncludeFunctionDecorator<Injection> = IncludeFunctionDecorator {
    IncludeFunctionScoped(scopes, SpikeNames.Singleton, it)
}

@Include(bindTo = BindTarget.Map)
@Order(30)
fun jsrNamed(names: NameLookup): IncludeFunctionDecorator<Injection> = IncludeFunctionDecorator {
    IncludeFunctionNamed(names, JsrNames.SpikeNamed, it)
}

@Include(bindTo = BindTarget.Map)
@Order(40)
fun jsrOriginating(): IncludeFunctionDecorator<Injection> = IncludeFunctionDecorator { IncludeFunctionOriginating(it) }
