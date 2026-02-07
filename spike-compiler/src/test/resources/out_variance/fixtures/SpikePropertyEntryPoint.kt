import kotlin.collections.Map
import kotlin.reflect.KClass

internal class SpikePropertyEntryPoint(
  private val container: SpikeDependencyContainer,
) : PropertyEntryPoint {
  override val cats: Map<KClass<out Cat>, Cat>
    get() = container.mapOfKClassOfoutCatAndCat
}

public operator fun PropertyEntryPoint.Companion.invoke(): PropertyEntryPoint = SpikePropertyEntryPointFactory().create()
