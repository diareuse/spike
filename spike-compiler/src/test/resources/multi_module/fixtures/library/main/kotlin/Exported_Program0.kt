@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName")

import kotlin.Any
import kotlin.Array
import kotlin.String
import kotlin.Suppress
import kotlin.arrayOfNulls

internal object Exported_Program0 {
  public fun resolve(factory: Exported_Factory): Any {
    val instances = arrayOfNulls<Any>(2)
    part0(factory, instances)
    return instances[1]!!
  }

  private fun part0(factory: Exported_Factory, instances: Array<Any?>) {
    instances[0] = factory.kotlinString
    instances[1] = Lib(instances[0] as String)
  }
}
