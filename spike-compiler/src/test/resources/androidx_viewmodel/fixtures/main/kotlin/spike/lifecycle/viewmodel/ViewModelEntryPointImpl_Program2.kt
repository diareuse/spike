@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName", "USELESS_CAST")

package spike.lifecycle.viewmodel

import MyViewModel
import androidx.lifecycle.SavedStateHandle
import kotlin.Any
import kotlin.Suppress

internal object ViewModelEntryPointImpl_Program2 {
  public fun resolve(factory: ViewModelEntryPointImpl_Factory): Any? = part0(factory)

  private fun part0(factory: ViewModelEntryPointImpl_Factory): Any? {
    val v0: SavedStateHandle = factory.handle
    return MyViewModel(v0)
  }
}
