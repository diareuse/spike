package spike.lifecycle.viewmodel

import androidx.lifecycle.SavedStateHandle
import spike.EntryPoint

@EntryPoint
public interface ViewModelEntryPointImpl : ViewModelEntryPoint {
  @EntryPoint.Factory
  public interface Factory : ViewModelEntryPoint.Factory {
    override fun create(handle: SavedStateHandle): ViewModelEntryPointImpl
  }

  public companion object
}
