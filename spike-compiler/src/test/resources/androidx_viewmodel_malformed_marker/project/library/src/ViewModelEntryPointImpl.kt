@file:OptIn(spike.InternalSpikeApi::class)

package spike.lifecycle.viewmodel

import androidx.lifecycle.SavedStateHandle

@spike.EntryPoint
interface ViewModelEntryPointImpl : ViewModelEntryPoint {
    @spike.EntryPoint.Factory
    @spike.EntryPoint.Factory.SharesSingletons
    interface Factory : ViewModelEntryPoint.Factory {
        override fun create(handle: SavedStateHandle): ViewModelEntryPointImpl
    }

    companion object
}
