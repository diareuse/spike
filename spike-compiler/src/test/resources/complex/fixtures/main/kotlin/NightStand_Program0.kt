@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName", "USELESS_CAST")

import kotlin.Any
import kotlin.Lazy
import kotlin.Suppress
import kotlin.lazy
import spike.factory.DependencyId

internal object NightStand_Program0 {
  public fun resolve(factory: NightStand_Factory): Any? = part0(factory)

  private fun part0(factory: NightStand_Factory): Any? {
    val v0: Lazy<SoundSystem> = lazy { factory.get<SoundSystem>(DependencyId(1)) }
    val v1: SamsungTelevision = factory._singletons.getOrPut(2) {
        SamsungTelevision(v0)
    }
    return v1.asTelevision
  }
}
