@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.Suppress
import spike.factory.DependencyId
import spike.factory.SingletonHolder

public class CatEP_Factory(
  public val singletons: SingletonHolder = SingletonHolder(),
) {
  @Suppress("UNCHECKED_CAST")
  public fun <T> `get`(id: DependencyId): T = when (id.id) {
    0 -> CatEP_Program0.resolve(this)
    else -> error("Invalid identifier $id")
  } as T
}
