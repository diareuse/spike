package spike.lifecycle.viewmodel

import MyViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlin.Any
import kotlin.Array
import kotlin.Int
import kotlin.collections.mapOf
import kotlin.reflect.KClass
import spike.Provider
import spike.factory.DependencyId

public class ViewModelEntryPoint_DependencyHolder0(
  private val factory: ViewModelEntryPoint_Factory,
) {
  internal fun create(buffer: Array<Any?>, position: Int): Any = when(position) {
    0 -> mapOf<KClass<out ViewModel>, Provider<ViewModel>>(
      MyViewModel::class to Provider { factory.get<MyViewModel>(DependencyId(3)) }
    )
    1 -> Provider { factory.get<MyViewModel>(DependencyId(3)) }
    2 -> factory.handle
    3 -> MyViewModel(buffer[0] as SavedStateHandle)
    else -> error("Invalid position")
  }
}
