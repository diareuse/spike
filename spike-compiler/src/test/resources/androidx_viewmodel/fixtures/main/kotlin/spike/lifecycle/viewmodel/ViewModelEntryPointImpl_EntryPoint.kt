@file:Suppress("ClassName", "RedundantVisibilityModifier")

package spike.lifecycle.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.reflect.KClass
import spike.Provider

private class ViewModelEntryPointImpl_EntryPoint(
  private val factory: ViewModelEntryPointImpl_Factory,
) : ViewModelEntryPointImpl {
  override val viewModels: Map<KClass<out ViewModel>, Provider<ViewModel>>
    get() = factory.get(spike.factory.DependencyId(0))

  public object Factory : ViewModelEntryPointImpl.Factory {
    override fun create(handle: SavedStateHandle): ViewModelEntryPointImpl = ViewModelEntryPointImpl_EntryPoint(ViewModelEntryPointImpl_Factory(handle))
  }
}

public operator fun ViewModelEntryPointImpl.Companion.invoke(handle: SavedStateHandle): ViewModelEntryPointImpl = ViewModelEntryPointImpl_EntryPoint.Factory.create(handle)

public fun ViewModelEntryPointImpl.Companion.factory(): ViewModelEntryPointImpl.Factory = ViewModelEntryPointImpl_EntryPoint.Factory
