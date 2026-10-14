@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import spike.factory.DependencyId
import spike.factory.SingletonHolder
import spike.factory.SingletonOwner

public class PropertyEntryPoint_Factory(
  singletons: SingletonHolder? = null,
) : SingletonOwner(singletons),
    PropertyEntryPoint {
  override val workers: Map<String, IntWorker>
    get() = get(DependencyId(0))

  @Suppress("UNCHECKED_CAST")
  public fun <T> `get`(id: DependencyId): T = when (id.id) {
    0 -> PropertyEntryPoint_Program0.resolve(this)
    2 -> PropertyEntryPoint_Program1.resolve(this)
    1 -> PropertyEntryPoint_Program2.resolve(this)
    else -> error("Invalid identifier $id")
  } as T
}
