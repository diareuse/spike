@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName")

import kotlin.Any
import kotlin.Array
import kotlin.Suppress
import kotlin.arrayOfNulls
import kotlin.collections.setOf

internal object PropertyEntryPoint_Program6 {
  public fun resolve(factory: PropertyEntryPoint_Factory): Any {
    val instances = arrayOfNulls<Any>(3)
    part0(factory, instances)
    return instances[2]!!
  }

  private fun part0(factory: PropertyEntryPoint_Factory, instances: Array<Any?>) {
    instances[0] = abyssinian()
    instances[1] = ragdoll()
    instances[2] = setOf<Cat>(instances[1] as Cat, instances[0] as Cat)
  }
}
