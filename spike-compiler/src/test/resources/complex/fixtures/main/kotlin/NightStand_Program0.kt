@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName", "USELESS_CAST")

import kotlin.Any
import kotlin.BooleanArray
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.lazy
import spike.factory.DependencyId

internal object NightStand_Program0 {
  private const val guards: String = "\u0002\u0002\u0000\u0002"

  public fun resolve(factory: NightStand_Factory): Any? {
    val needed = factory._singletons.needed(guards)
    return part0(factory, needed)
  }

  private fun part0(factory: NightStand_Factory, needed: BooleanArray): Any? {
    val v0: Lazy<SoundSystem>? = if (!needed[0]) null else lazy { factory.get<SoundSystem>(DependencyId(2)) }
    val v1: SamsungTelevision = factory._singletons.getOrPut(1) {
        SamsungTelevision((v0 as Lazy<SoundSystem>))
    }
    return v1.asTelevision
  }
}
