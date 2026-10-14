@file:Suppress("ClassName", "RedundantVisibilityModifier")

package spike.lifecycle.viewmodel

import androidx.lifecycle.SavedStateHandle
import kotlin.Suppress
import spike.factory.DependencyId
import spike.factory.SingletonHolder

public class ViewModelEntryPointImpl_Factory(
  public val handle: SavedStateHandle,
  public val singletons: SingletonHolder = SingletonHolder(),
) {
  @Suppress("UNCHECKED_CAST")
  public fun <T> `get`(id: DependencyId): T = when (id.id) {
    0 -> ViewModelEntryPointImpl_Program0.resolve(this)
    2 -> ViewModelEntryPointImpl_Program1.resolve(this)
    1 -> ViewModelEntryPointImpl_Program2.resolve(this)
    else -> error("Invalid identifier $id")
  } as T
}
