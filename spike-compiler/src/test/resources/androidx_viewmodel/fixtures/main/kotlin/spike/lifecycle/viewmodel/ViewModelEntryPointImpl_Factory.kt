@file:Suppress("ClassName", "RedundantVisibilityModifier")

package spike.lifecycle.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.reflect.KClass
import spike.Provider
import spike.factory.DependencyId
import spike.factory.SingletonHolder
import spike.factory.SingletonOwner

public class ViewModelEntryPointImpl_Factory(
  internal val _handle: SavedStateHandle,
  singletons: SingletonHolder? = null,
) : SingletonOwner(singletons),
    ViewModelEntryPointImpl {
  override val viewModels: Map<KClass<out ViewModel>, Provider<ViewModel>>
    get() = get(DependencyId(0))

  @Suppress("UNCHECKED_CAST")
  public fun <T> `get`(id: DependencyId): T = when (id.id) {
    0 -> ViewModelEntryPointImpl_Program0.resolve(this)
    2 -> ViewModelEntryPointImpl_Program1.resolve(this)
    1 -> ViewModelEntryPointImpl_Program2.resolve(this)
    else -> error("Invalid identifier $id")
  } as T
}
