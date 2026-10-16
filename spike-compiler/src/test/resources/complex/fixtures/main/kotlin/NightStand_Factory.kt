@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.Suppress
import spike.factory.DependencyId
import spike.factory.SingletonHolder
import spike.factory.SingletonOwner

public class NightStand_Factory(
  singletons: SingletonHolder? = null,
) : SingletonOwner(singletons),
    NightStand {
  override val television: Television
    get() = get(DependencyId(0))

  override val remote: Remote
    get() = get(DependencyId(4))

  @Suppress("UNCHECKED_CAST")
  public fun <T> `get`(id: DependencyId): T = when (id.id) {
    0 -> NightStand_Program0.resolve(this)
    2 -> NightStand_Program1.resolve(this)
    4 -> NightStand_Program2.resolve(this)
    else -> error("Invalid identifier $id")
  } as T
}
