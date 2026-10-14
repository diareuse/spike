@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName")

import kotlin.Any
import kotlin.Array
import kotlin.Lazy
import kotlin.Suppress
import kotlin.arrayOfNulls
import kotlin.lazy
import spike.factory.DependencyId

internal object NightStand_Program0 {
  public fun resolve(factory: NightStand_Factory): Any {
    val instances = arrayOfNulls<Any>(3)
    part0(factory, instances)
    return instances[2]!!
  }

  private fun part0(factory: NightStand_Factory, instances: Array<Any?>) {
    instances[0] = lazy { factory.get<SoundSystem>(DependencyId(1)) }
    instances[1] = factory.singletons.getOrPut(2) {
        SamsungTelevision(instances[0] as Lazy<SoundSystem>)
    }
    instances[2] = instances[1] as Television
  }
}
