package spike.lifecycle.viewmodel

import androidx.lifecycle.SavedStateHandle

internal object SpikeViewModelEntryPointFactory : ViewModelEntryPoint.Factory {
  override fun create(handle: SavedStateHandle): ViewModelEntryPoint = SpikeViewModelEntryPoint(SpikeDependencyContainer(handle))
}
