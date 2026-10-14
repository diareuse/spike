@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName")

import kotlin.Any
import kotlin.Array
import kotlin.Suppress
import kotlin.arrayOfNulls
import spike.Provider
import spike.factory.DependencyId

internal object PropertyEntryPoint_Program0 {
  public fun resolve(factory: PropertyEntryPoint_Factory): Any {
    val instances = arrayOfNulls<Any>(2)
    part0(factory, instances)
    return instances[1]!!
  }

  private fun part0(factory: PropertyEntryPoint_Factory, instances: Array<Any?>) {
    instances[0] = Provider { factory.get<Food>(DependencyId(1)) }
    instances[1] = Cat(instances[0] as Provider<Food>)
  }
}
