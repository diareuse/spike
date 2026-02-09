package spike.lifecycle.viewmodel

import MyViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlin.collections.Map
import kotlin.collections.mapOf
import kotlin.reflect.KClass
import spike.Provider

internal class SpikeDependencyContainer(
  private val savedStateHandle: SavedStateHandle,
) {
  public val mapOfKClassOfoutViewModelAndProviderOfViewModel:
      Map<KClass<out ViewModel>, Provider<ViewModel>>
    get() = mapOf(MyViewModel::class to Provider {
        MyViewModel()
      }
    )
}
