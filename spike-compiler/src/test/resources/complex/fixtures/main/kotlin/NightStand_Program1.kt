@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName")

import kotlin.Any
import kotlin.Array
import kotlin.Suppress
import kotlin.arrayOfNulls

internal object NightStand_Program1 {
  public fun resolve(factory: NightStand_Factory): Any {
    val instances = arrayOfNulls<Any>(2)
    part0(factory, instances)
    return instances[1]!!
  }

  private fun part0(factory: NightStand_Factory, instances: Array<Any?>) {
    instances[0] = AmazonFireSoundBar()
    instances[1] = instances[0] as SoundSystem
  }
}
