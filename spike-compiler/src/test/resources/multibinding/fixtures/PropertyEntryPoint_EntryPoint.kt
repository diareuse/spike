import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.Set

private object PropertyEntryPoint_EntryPoint : PropertyEntryPoint {
  override val catsWithNames: Map<String, Cat>
    get() = PropertyEntryPoint_Factory.get(spike.factory.DependencyId(0))

  override val cats: List<Cat>
    get() = PropertyEntryPoint_Factory.get(spike.factory.DependencyId(3))

  override val uniqueCats: Set<Cat>
    get() = PropertyEntryPoint_Factory.get(spike.factory.DependencyId(6))
}

public operator fun PropertyEntryPoint.Companion.invoke(): PropertyEntryPoint = PropertyEntryPoint_EntryPoint
