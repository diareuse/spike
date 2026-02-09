package spike.lifecycle.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlin.collections.Map
import kotlin.reflect.KClass
import spike.Provider

internal class SpikeViewModelEntryPoint(
  private val container: SpikeDependencyContainer,
) : ViewModelEntryPoint {
  override val viewModels: Map<KClass<out ViewModel>, Provider<ViewModel>>
    get() = container.mapOfKClassOfoutViewModelAndProviderOfViewModel
}

public operator fun ViewModelEntryPoint.Companion.invoke(handle: SavedStateHandle): ViewModelEntryPoint = SpikeViewModelEntryPointFactory().create(handle)
