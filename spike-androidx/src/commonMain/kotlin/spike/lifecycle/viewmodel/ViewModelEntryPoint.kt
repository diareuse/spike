package spike.lifecycle.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import spike.EntryPoint
import spike.Provider
import kotlin.reflect.KClass

@EntryPoint
interface ViewModelEntryPoint {
    val viewModels: Map<KClass<out ViewModel>, Provider<ViewModel>>
    @EntryPoint.Factory
    interface Factory {
        fun create(handle: SavedStateHandle): ViewModelEntryPoint
    }
    companion object
}