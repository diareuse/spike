@file:Suppress("ClassName", "RedundantVisibilityModifier")

package spike.lifecycle.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.reflect.KClass
import spike.Provider

private class ViewModelEntryPoint_EntryPoint(
  private val factory: ViewModelEntryPoint_Factory,
) : ViewModelEntryPoint {
  override val viewModels: Map<KClass<out ViewModel>, Provider<ViewModel>>
    get() = factory.get(spike.factory.DependencyId(0))

  public object Factory : ViewModelEntryPoint.Factory {
    override fun create(handle: SavedStateHandle): ViewModelEntryPoint = ViewModelEntryPoint_EntryPoint(ViewModelEntryPoint_Factory(handle))
  }
}

public operator fun ViewModelEntryPoint.Companion.invoke(handle: SavedStateHandle): ViewModelEntryPoint = ViewModelEntryPoint_EntryPoint.Factory.create(handle)

public fun ViewModelEntryPoint.Companion.factory(): ViewModelEntryPoint.Factory = ViewModelEntryPoint_EntryPoint.Factory
