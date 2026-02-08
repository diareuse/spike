package spike.lifecycle.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass

class ViewModelProviderFactory(
    private val entryPoint: ViewModelEntryPoint.Factory,
    private val origin: ViewModelProvider.Factory
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras
    ): T {
        val handle = extras.createSavedStateHandle()
        return when(val provider = entryPoint.create(handle).viewModels[modelClass]) {
            null -> origin.create(modelClass, extras)
            else -> provider.get() as T
        }
    }
}
