@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName", "USELESS_CAST")

package spike.lifecycle.viewmodel

import androidx.lifecycle.SavedStateHandle
import kotlin.Any
import kotlin.Suppress
import spikeViewModelMyViewModel

internal object ViewModelEntryPointImpl_Program1 {
  public fun resolve(factory: ViewModelEntryPointImpl_Factory): Any? = part0(factory)

  private fun part0(factory: ViewModelEntryPointImpl_Factory): Any? {
    val v0: SavedStateHandle = factory._handle
    return spikeViewModelMyViewModel(v0)
  }
}
