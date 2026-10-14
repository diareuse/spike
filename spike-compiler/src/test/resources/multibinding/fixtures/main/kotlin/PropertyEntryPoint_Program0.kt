@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName")

import kotlin.Any
import kotlin.Array
import kotlin.String
import kotlin.Suppress
import kotlin.arrayOfNulls
import kotlin.collections.mapOf

internal object PropertyEntryPoint_Program0 {
  public fun resolve(factory: PropertyEntryPoint_Factory): Any {
    val instances = arrayOfNulls<Any>(3)
    part0(factory, instances)
    return instances[2]!!
  }

  private fun part0(factory: PropertyEntryPoint_Factory, instances: Array<Any?>) {
    instances[0] = ragamuffin()
    instances[1] = sphynx()
    instances[2] = mapOf<String, Cat>(
      "sphynx" to instances[1] as Cat,
      "ragamuffin" to instances[0] as Cat
    )
  }
}
