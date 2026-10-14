@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName")

import kotlin.Any
import kotlin.Array
import kotlin.String
import kotlin.Suppress
import kotlin.arrayOfNulls
import spike.Provider
import spike.factory.DependencyId

internal object PropertyEntryPoint_Program1 {
  public fun resolve(factory: PropertyEntryPoint_Factory): Any {
    val instances = arrayOfNulls<Any>(1)
    part0(factory, instances)
    return instances[0]!!
  }

  private fun part0(factory: PropertyEntryPoint_Factory, instances: Array<Any?>) {
    instances[0] = Provider { factory.get<String>(DependencyId(2)) }
  }
}
