@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName")

import kotlin.Any
import kotlin.Array
import kotlin.Suppress
import kotlin.arrayOfNulls
import kotlin.collections.mapOf

internal object PropertyEntryPoint_Program2 {
  public fun resolve(factory: PropertyEntryPoint_Factory): Any {
    val instances = arrayOfNulls<Any>(2)
    part0(factory, instances)
    return instances[1]!!
  }

  private fun part0(factory: PropertyEntryPoint_Factory, instances: Array<Any?>) {
    instances[0] = redCat()
    instances[1] = mapOf<CatType, Cat>(
      CatType.Red to instances[0] as Cat
    )
  }
}
