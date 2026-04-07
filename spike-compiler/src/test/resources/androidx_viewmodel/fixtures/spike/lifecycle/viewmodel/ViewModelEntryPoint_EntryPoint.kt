package spike.lifecycle.viewmodel

import androidx.lifecycle.ViewModel
import kotlin.collections.Map
import kotlin.reflect.KClass
import spike.Provider

private object ViewModelEntryPoint_EntryPoint : ViewModelEntryPoint {
  override val viewModels: Map<KClass<out ViewModel>, Provider<ViewModel>>
    get() = ViewModelEntryPoint_Factory.get(spike.factory.DependencyId(0))
}

public operator fun ViewModelEntryPoint.Companion.invoke(): ViewModelEntryPoint = ViewModelEntryPoint_EntryPoint
