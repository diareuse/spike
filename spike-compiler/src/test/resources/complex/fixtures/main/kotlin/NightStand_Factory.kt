@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.Suppress
import spike.factory.DependencyId
import spike.factory.SingletonHolder

public class NightStand_Factory(
  public val singletons: SingletonHolder = SingletonHolder(),
) {
  @Suppress("UNCHECKED_CAST")
  public fun <T> `get`(id: DependencyId): T = when (id.id) {
    0 -> NightStand_Program0.resolve(this)
    1 -> NightStand_Program1.resolve(this)
    4 -> NightStand_Program2.resolve(this)
    else -> error("Invalid identifier $id")
  } as T
}
