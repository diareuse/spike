@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName")

package spike.lifecycle.viewmodel

import MyViewModel
import androidx.lifecycle.SavedStateHandle
import kotlin.Any
import kotlin.Array
import kotlin.Suppress
import kotlin.arrayOfNulls

internal object ViewModelEntryPointImpl_Program2 {
  public fun resolve(factory: ViewModelEntryPointImpl_Factory): Any {
    val instances = arrayOfNulls<Any>(2)
    part0(factory, instances)
    return instances[1]!!
  }

  private fun part0(factory: ViewModelEntryPointImpl_Factory, instances: Array<Any?>) {
    instances[0] = factory.handle
    instances[1] = MyViewModel(instances[0] as SavedStateHandle)
  }
}
