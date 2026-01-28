import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.Set
import kotlin.collections.listOf
import kotlin.collections.mapOf
import kotlin.collections.setOf

internal class SpikeDependencyContainer() {
  public val mapOfStringAndCat: Map<String, Cat>
    get() = mapOf("sphynx" to sphynx(), "ragamuffin" to ragamuffin())

  public inline val listOfCat: List<Cat>
    get() = listOf(toyger(), bombay())

  public inline val setOfCat: Set<Cat>
    get() = setOf(ragdoll(), abyssinian())
}
