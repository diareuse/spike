@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName")

import kotlin.Any
import kotlin.Array
import kotlin.Suppress
import kotlin.arrayOfNulls

internal object PropertyEntryPoint_Program0 {
  public fun resolve(factory: PropertyEntryPoint_Factory): Any {
    val instances = arrayOfNulls<Any>(4)
    part0(factory, instances)
    return instances[3]!!
  }

  private fun part0(factory: PropertyEntryPoint_Factory, instances: Array<Any?>) {
    instances[0] = Cat()
    instances[1] = Box(instances[0] as Cat)
    instances[2] = TeleportedCat(instances[1] as Box, instances[0] as Cat)
    instances[3] = ComplexObject(instances[0] as Cat, instances[1] as Box, instances[2] as TeleportedCat)
  }
}
