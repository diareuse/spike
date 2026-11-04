@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.Suppress
import spike.factory.DependencyId
import spike.factory.SingletonHolder
import spike.factory.SingletonOwner

public class Odd_Factory(
  singletons: SingletonHolder? = null,
) : SingletonOwner(singletons),
    Odd {
  override val plain: Plain
    get() = get(DependencyId(0))

  @Suppress("UNCHECKED_CAST")
  public fun <T> `get`(id: DependencyId): T = when (id.id) {
    0 -> Odd_Program0.resolve(this)
    else -> error("Invalid identifier $id")
  } as T
}
