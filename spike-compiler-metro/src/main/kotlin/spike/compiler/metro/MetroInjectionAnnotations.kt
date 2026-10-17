package spike.compiler.metro

import spike.BindTarget
import spike.Include
import spike.compiler.conversion.IncludeFunctionDecorator
import spike.compiler.conversion.IncludeFunctionInternal
import spike.compiler.conversion.Injection
import spike.compiler.conversion.NameLookup
import spike.compiler.conversion.Order
import spike.compiler.conversion.ScopeLookup

@Include(bindTo = BindTarget.Map)
@Order(5)
fun metroInternal(): IncludeFunctionDecorator<Injection> = IncludeFunctionDecorator { IncludeFunctionInternal(it) }

@Include(bindTo = BindTarget.Map)
@Order(10)
fun metroInjectionIntoMap(): IncludeFunctionDecorator<Injection> = MetroAnnotations.intoMap()

@Include(bindTo = BindTarget.Map)
@Order(20)
fun metroInjectionIntoSet(): IncludeFunctionDecorator<Injection> = MetroAnnotations.intoSet()

@Include(bindTo = BindTarget.Map)
@Order(30)
fun metroInjectionScoped(scopes: ScopeLookup): IncludeFunctionDecorator<Injection> = MetroAnnotations.scoped(scopes)

@Include(bindTo = BindTarget.Map)
@Order(40)
fun metroInjectionNamed(names: NameLookup): IncludeFunctionDecorator<Injection> = MetroAnnotations.named(names)

@Include(bindTo = BindTarget.Map)
@Order(50)
fun metroInjectionMapKey(): IncludeFunctionDecorator<Injection> = MetroAnnotations.mapKey()

@Include(bindTo = BindTarget.Map)
@Order(60)
fun metroInjectionOriginating(): IncludeFunctionDecorator<Injection> = MetroAnnotations.originating()
