import kotlin.collections.Map
import kotlin.collections.mapOf
import kotlin.reflect.KClass

internal class SpikeDependencyContainer() {
  public val mapOfKClassOfoutCatAndCat: Map<KClass<out Cat>, Cat>
    get() = mapOf()
}
