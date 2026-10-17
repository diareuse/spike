package spike.compiler.metro

import spike.BindTarget
import spike.Include
import spike.compiler.conversion.IncludeFunctionDecorator
import spike.compiler.conversion.NameLookup
import spike.compiler.conversion.Order
import spike.compiler.conversion.Provision
import spike.compiler.conversion.ScopeLookup

@Include(bindTo = BindTarget.Map)
@Order(10)
fun metroProvisionIntoMap(): IncludeFunctionDecorator<Provision> = MetroAnnotations.intoMap()

@Include(bindTo = BindTarget.Map)
@Order(20)
fun metroProvisionIntoSet(): IncludeFunctionDecorator<Provision> = MetroAnnotations.intoSet()

@Include(bindTo = BindTarget.Map)
@Order(30)
fun metroProvisionScoped(scopes: ScopeLookup): IncludeFunctionDecorator<Provision> = MetroAnnotations.scoped(scopes)

@Include(bindTo = BindTarget.Map)
@Order(40)
fun metroProvisionNamed(names: NameLookup): IncludeFunctionDecorator<Provision> = MetroAnnotations.named(names)

@Include(bindTo = BindTarget.Map)
@Order(50)
fun metroProvisionMapKey(): IncludeFunctionDecorator<Provision> = MetroAnnotations.mapKey()

@Include(bindTo = BindTarget.Map)
@Order(60)
fun metroProvisionOriginating(): IncludeFunctionDecorator<Provision> = MetroAnnotations.originating()
