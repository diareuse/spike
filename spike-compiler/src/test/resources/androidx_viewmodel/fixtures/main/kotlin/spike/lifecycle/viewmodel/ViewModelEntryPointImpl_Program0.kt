@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName")

package spike.lifecycle.viewmodel

import MyViewModel
import androidx.lifecycle.ViewModel
import kotlin.Any
import kotlin.Array
import kotlin.Suppress
import kotlin.arrayOfNulls
import kotlin.collections.mapOf
import kotlin.reflect.KClass
import spike.Provider
import spike.factory.DependencyId

internal object ViewModelEntryPointImpl_Program0 {
  public fun resolve(factory: ViewModelEntryPointImpl_Factory): Any {
    val instances = arrayOfNulls<Any>(2)
    part0(factory, instances)
    return instances[1]!!
  }

  private fun part0(factory: ViewModelEntryPointImpl_Factory, instances: Array<Any?>) {
    instances[0] = Provider { factory.get<MyViewModel>(DependencyId(1)) }
    instances[1] = mapOf<KClass<out ViewModel>, Provider<ViewModel>>(
      MyViewModel::class to instances[0] as Provider<ViewModel>
    )
  }
}
