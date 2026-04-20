import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.Set

private class PropertyEntryPoint_EntryPoint(
  private val factory: PropertyEntryPoint_Factory,
) : PropertyEntryPoint {
  override val catsWithNames: Map<String, Cat>
    get() = factory.get(spike.factory.DependencyId(0))

  override val cats: List<Cat>
    get() = factory.get(spike.factory.DependencyId(3))

  override val uniqueCats: Set<Cat>
    get() = factory.get(spike.factory.DependencyId(6))

  public object Factory {
    public fun create(): PropertyEntryPoint_EntryPoint = PropertyEntryPoint_EntryPoint(PropertyEntryPoint_Factory())
  }
}

public operator fun PropertyEntryPoint.Companion.invoke(): PropertyEntryPoint = PropertyEntryPoint_EntryPoint.Factory.create()
