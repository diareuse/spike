@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName", "USELESS_CAST")

import kotlin.Any
import kotlin.Suppress
import kotlin.collections.List
import spike.Provider
import spike.factory.DependencyId

internal object NightStand_Program2 {
  public fun resolve(factory: NightStand_Factory): Any? = part0(factory)

  private fun part0(factory: NightStand_Factory): Any? {
    val v0: List<Battery> = batteries()
    val v1: Provider<Television> = Provider { factory.get<Television>(DependencyId(0)) }
    val v2: RemoteControl = RemoteControl(v1, v0)
    return v2.asRemote
  }
}
