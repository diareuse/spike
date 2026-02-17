package spike.lifecycle.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass

class ViewModelProviderFactory private constructor(
    private val entryPoint: ViewModelEntryPoint.Factory,
    private val origin: ViewModelProvider.Factory
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras
    ): T {
        val handle = extras.createSavedStateHandle()
        return when (val provider = entryPoint.create(handle).viewModels[modelClass]) {
            null -> origin.create(modelClass, extras)
            else -> provider.get() as T
        }
    }

    companion object {
        /**
         * Use this provider to supply [ViewModel]s to your components.
         *
         * ## Android
         *
         * Override the following method in your Activity:
         *
         * ```
         * override val defaultViewModelProviderFactory by lazy {
         *     ViewModelProviderFactory.from(this, ViewModelEntryPoint.factory)
         * }
         * ```
         *
         * Thereafter, you can use the usual syntax:
         *
         * ```
         * val viewModel: MyViewModel by viewModels()
         * ```
         *
         * @param owner is usually your Activity or Fragment on Android, otherwise your scoped component with this
         *   interface
         * @param entryPoint is instantiated as `ViewModelEntryPoint.factory` and passed directly here. The callable
         *   function is generated through KSP at build time. If your graph requires additional dependencies, you need
         *   to create your own [ViewModelEntryPoint] and its associated [ViewModelEntryPoint.Factory]. That said, it's
         *   the best practice not to include such dependencies as binding arbitrary primitive values to a graph is
         *   going to wreak havoc.
         * */
        fun from(
            owner: ViewModelStoreOwner,
            entryPoint: ViewModelEntryPoint.Factory
        ): ViewModelProvider.Factory? = when (owner) {
            is HasDefaultViewModelProviderFactory -> ViewModelProviderFactory(
                entryPoint = entryPoint,
                origin = owner.defaultViewModelProviderFactory
            )

            else -> null
        }
    }
}
