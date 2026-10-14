@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName")

import kotlin.Any
import kotlin.Array
import kotlin.Suppress
import kotlin.arrayOfNulls
import kotlin.collections.mapOf
import kotlin.reflect.KClass

internal object PropertyEntryPoint_Program0 {
  public fun resolve(factory: PropertyEntryPoint_Factory): Any {
    val instances = arrayOfNulls<Any>(2)
    part0(factory, instances)
    return instances[1]!!
  }

  private fun part0(factory: PropertyEntryPoint_Factory, instances: Array<Any?>) {
    instances[0] = SomeCat()
    instances[1] = mapOf<KClass<out Cat>, Cat>(
      SomeCat::class to instances[0] as Cat
    )
  }
}
