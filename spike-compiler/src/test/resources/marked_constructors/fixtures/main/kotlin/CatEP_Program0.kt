@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName")

import kotlin.Any
import kotlin.Array
import kotlin.Suppress
import kotlin.arrayOfNulls

internal object CatEP_Program0 {
  public fun resolve(factory: CatEP_Factory): Any {
    val instances = arrayOfNulls<Any>(2)
    part0(factory, instances)
    return instances[1]!!
  }

  private fun part0(factory: CatEP_Factory, instances: Array<Any?>) {
    instances[0] = Life()
    instances[1] = Cat(instances[0] as Life, instances[0] as Life, instances[0] as Life)
  }
}
