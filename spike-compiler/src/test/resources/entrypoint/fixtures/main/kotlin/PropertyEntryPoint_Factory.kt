@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.Suppress
import spike.factory.DependencyId
import spike.factory.SingletonHolder
import spike.factory.SingletonOwner

public class PropertyEntryPoint_Factory(
  singletons: SingletonHolder? = null,
) : SingletonOwner(singletons),
    PropertyEntryPoint {
  override val injectable: Injectable
    get() = get(DependencyId(0))

  @Suppress("UNCHECKED_CAST")
  public fun <T> `get`(id: DependencyId): T = when (id.id) {
    0 -> PropertyEntryPoint_Program0.resolve(this)
    else -> error("Invalid identifier $id")
  } as T
}
