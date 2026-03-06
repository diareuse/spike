package spike.lifecycle.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import spike.EntryPoint
import spike.Provider
import kotlin.reflect.KClass

@EntryPoint
public interface ViewModelEntryPoint {
    public val viewModels: Map<KClass<out ViewModel>, Provider<ViewModel>>
    @EntryPoint.Factory
    public interface Factory {
        public fun create(handle: SavedStateHandle): ViewModelEntryPoint
    }
    public companion object
}