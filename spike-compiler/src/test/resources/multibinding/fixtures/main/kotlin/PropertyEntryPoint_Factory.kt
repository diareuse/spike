@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.Set
import spike.factory.DependencyId
import spike.factory.SingletonHolder
import spike.factory.SingletonOwner

public class PropertyEntryPoint_Factory(
  singletons: SingletonHolder? = null,
) : SingletonOwner(singletons),
    PropertyEntryPoint {
  override val catsWithNames: Map<String, Cat>
    get() = get(DependencyId(0))

  override val cats: List<Cat>
    get() = get(DependencyId(3))

  override val uniqueCats: Set<Cat>
    get() = get(DependencyId(6))

  @Suppress("UNCHECKED_CAST")
  public fun <T> `get`(id: DependencyId): T = when (id.id) {
    0 -> PropertyEntryPoint_Program0.resolve(this)
    3 -> PropertyEntryPoint_Program1.resolve(this)
    6 -> PropertyEntryPoint_Program2.resolve(this)
    else -> error("Invalid identifier $id")
  } as T
}
