import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.Set

internal class SpikePropertyEntryPoint(
  private val container: SpikeDependencyContainer,
) : PropertyEntryPoint {
  override val catsWithNames: Map<String, Cat>
    get() = container.mapOfStringAndCat

  override val cats: List<Cat>
    get() = container.listOfCat

  override val uniqueCats: Set<Cat>
    get() = container.setOfCat
}

public operator fun PropertyEntryPoint.Companion.invoke(): PropertyEntryPoint = SpikePropertyEntryPointFactory.create()
