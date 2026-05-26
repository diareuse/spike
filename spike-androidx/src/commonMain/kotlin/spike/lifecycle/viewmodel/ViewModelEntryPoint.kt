package spike.lifecycle.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import spike.Provider
import kotlin.reflect.KClass

public interface ViewModelEntryPoint {
    public val viewModels: Map<KClass<out ViewModel>, Provider<ViewModel>>
    public interface Factory {
        public fun create(handle: SavedStateHandle): ViewModelEntryPoint
    }
}