@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName")

package spike.lifecycle.viewmodel

import MyViewModel
import kotlin.Any
import kotlin.Array
import kotlin.Suppress
import kotlin.arrayOfNulls
import spike.Provider
import spike.factory.DependencyId

internal object ViewModelEntryPointImpl_Program1 {
  public fun resolve(factory: ViewModelEntryPointImpl_Factory): Any {
    val instances = arrayOfNulls<Any>(2)
    part0(factory, instances)
    return instances[1]!!
  }

  private fun part0(factory: ViewModelEntryPointImpl_Factory, instances: Array<Any?>) {
    instances[0] = factory.handle
    instances[1] = Provider { factory.get<MyViewModel>(DependencyId(1)) }
  }
}
