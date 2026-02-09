package spike.lifecycle.viewmodel

import androidx.lifecycle.SavedStateHandle

internal class SpikeViewModelEntryPointFactory : ViewModelEntryPoint.Factory {
  override fun create(handle: SavedStateHandle): ViewModelEntryPoint = SpikeViewModelEntryPoint(SpikeDependencyContainer(handle))
}
