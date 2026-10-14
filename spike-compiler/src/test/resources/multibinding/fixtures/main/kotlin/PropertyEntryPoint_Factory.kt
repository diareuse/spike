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
    2 -> PropertyEntryPoint_Program1.resolve(this)
    1 -> PropertyEntryPoint_Program2.resolve(this)
    3 -> PropertyEntryPoint_Program3.resolve(this)
    5 -> PropertyEntryPoint_Program4.resolve(this)
    4 -> PropertyEntryPoint_Program5.resolve(this)
    6 -> PropertyEntryPoint_Program6.resolve(this)
    8 -> PropertyEntryPoint_Program7.resolve(this)
    7 -> PropertyEntryPoint_Program8.resolve(this)
    else -> error("Invalid identifier $id")
  } as T
}
