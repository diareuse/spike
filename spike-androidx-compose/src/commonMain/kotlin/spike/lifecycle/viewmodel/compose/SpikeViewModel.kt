package spike.lifecycle.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import spike.compose.LocalViewModelEntryPointFactory
import spike.lifecycle.viewmodel.ViewModelEntryPoint
import spike.lifecycle.viewmodel.ViewModelProviderFactory

@Composable
inline fun <reified VM : ViewModel> spikeViewModel(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    entryPoint: ViewModelEntryPoint.Factory = checkNotNull(LocalViewModelEntryPointFactory.current) {
        "No ViewModelEntryPoint was provided via LocalViewModelEntryPointFactory."
    },
    key: String? = null
): VM {
    val factory = createSpikeViewModelFactory(viewModelStoreOwner, entryPoint)
    return viewModel(
        viewModelStoreOwner = viewModelStoreOwner,
        key = key,
        factory = factory
    )
}

@Composable
@PublishedApi
internal fun createSpikeViewModelFactory(
    viewModelStoreOwner: ViewModelStoreOwner,
    entryPoint: ViewModelEntryPoint.Factory
) = ViewModelProviderFactory.from(
    owner = viewModelStoreOwner,
    entryPoint = entryPoint
)