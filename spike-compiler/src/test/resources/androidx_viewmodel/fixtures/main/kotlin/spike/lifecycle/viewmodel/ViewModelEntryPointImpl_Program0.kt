@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName", "USELESS_CAST")

package spike.lifecycle.viewmodel

import MyViewModel
import androidx.lifecycle.ViewModel
import kotlin.Any
import kotlin.Suppress
import kotlin.collections.mapOf
import kotlin.reflect.KClass
import spike.Provider
import spike.factory.DependencyId

internal object ViewModelEntryPointImpl_Program0 {
  public fun resolve(factory: ViewModelEntryPointImpl_Factory): Any? = part0(factory)

  private fun part0(factory: ViewModelEntryPointImpl_Factory): Any? {
    val v0: Provider<MyViewModel> = Provider { factory.get<MyViewModel>(DependencyId(1)) }
    return mapOf<KClass<out ViewModel>, Provider<ViewModel>>(
      MyViewModel::class to v0 as Provider<ViewModel>
    )
  }
}
