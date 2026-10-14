@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.Suppress
import spike.factory.DependencyId
import spike.factory.SingletonHolder
import spike.factory.SingletonOwner

public class CatEP_Factory(
  singletons: SingletonHolder? = null,
) : SingletonOwner(singletons),
    CatEP {
  override val cat: Cat
    get() = get(DependencyId(0))

  @Suppress("UNCHECKED_CAST")
  public fun <T> `get`(id: DependencyId): T = when (id.id) {
    0 -> CatEP_Program0.resolve(this)
    else -> error("Invalid identifier $id")
  } as T
}
