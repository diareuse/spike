package spike.lifecycle.viewmodel

import MyViewModel
import androidx.lifecycle.ViewModel
import kotlin.Any
import kotlin.Array
import kotlin.Int
import kotlin.collections.mapOf
import kotlin.reflect.KClass
import spike.Provider
import spike.factory.DependencyId

public object ViewModelEntryPoint_DependencyHolder0 {
  internal fun create(buffer: Array<Any?>, position: Int): Any = when(position) {
    0 -> mapOf<KClass<out ViewModel>, Provider<ViewModel>>(
      MyViewModel::class to Provider { ViewModelEntryPoint_Factory.get<MyViewModel>(DependencyId(2)) }
    )
    1 -> Provider { ViewModelEntryPoint_Factory.get<MyViewModel>(DependencyId(2)) }
    2 -> MyViewModel()
    else -> error("Invalid position")
  }
}
