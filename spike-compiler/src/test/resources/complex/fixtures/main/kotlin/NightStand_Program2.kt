@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName")

import kotlin.Any
import kotlin.Array
import kotlin.Suppress
import kotlin.arrayOfNulls
import kotlin.collections.List
import spike.Provider
import spike.factory.DependencyId

internal object NightStand_Program2 {
  public fun resolve(factory: NightStand_Factory): Any {
    val instances = arrayOfNulls<Any>(4)
    part0(factory, instances)
    return instances[3]!!
  }

  private fun part0(factory: NightStand_Factory, instances: Array<Any?>) {
    instances[0] = batteries()
    instances[1] = Provider { factory.get<Television>(DependencyId(0)) }
    instances[2] = RemoteControl(instances[1] as Provider<Television>, instances[0] as List<Battery>)
    instances[3] = instances[2] as Remote
  }
}
