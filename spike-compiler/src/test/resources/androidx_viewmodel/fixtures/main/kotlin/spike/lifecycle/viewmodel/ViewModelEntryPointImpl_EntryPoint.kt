@file:Suppress("ClassName", "RedundantVisibilityModifier")

package spike.lifecycle.viewmodel

import androidx.lifecycle.SavedStateHandle
import kotlin.Suppress
import spike.factory.SingletonHolder

private object ViewModelEntryPointImpl_EntryPoint {
  public object Factory : ViewModelEntryPointImpl.Factory {
    private val singletons: SingletonHolder = SingletonHolder()

    override fun create(handle: SavedStateHandle): ViewModelEntryPointImpl = ViewModelEntryPointImpl_Factory(handle, singletons = singletons)
  }
}

public operator fun ViewModelEntryPointImpl.Companion.invoke(handle: SavedStateHandle): ViewModelEntryPointImpl = ViewModelEntryPointImpl_EntryPoint.Factory.create(handle)

public fun ViewModelEntryPointImpl.Companion.factory(): ViewModelEntryPointImpl.Factory = ViewModelEntryPointImpl_EntryPoint.Factory
