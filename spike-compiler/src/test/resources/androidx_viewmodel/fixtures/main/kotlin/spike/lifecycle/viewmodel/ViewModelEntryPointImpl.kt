@file:OptIn(InternalSpikeApi::class)

package spike.lifecycle.viewmodel

import androidx.lifecycle.SavedStateHandle
import kotlin.OptIn
import spike.EntryPoint
import spike.InternalSpikeApi

@EntryPoint
public interface ViewModelEntryPointImpl : ViewModelEntryPoint {
  @EntryPoint.Factory
  @EntryPoint.Factory.SharesSingletons
  public interface Factory : ViewModelEntryPoint.Factory {
    override fun create(handle: SavedStateHandle): ViewModelEntryPointImpl
  }

  public companion object
}
