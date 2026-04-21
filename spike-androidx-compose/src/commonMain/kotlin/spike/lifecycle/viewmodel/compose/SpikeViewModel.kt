package spike.lifecycle.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.savedstate.SavedStateWriter
import androidx.savedstate.savedState
import androidx.savedstate.write
import spike.compose.LocalViewModelEntryPointFactory
import spike.lifecycle.viewmodel.ViewModelEntryPoint
import spike.lifecycle.viewmodel.ViewModelProviderFactory

/**
 * Creates new ViewModel instance with Spike.
 * @param extras see [rememberNavigationExtras]
 * @see viewModel
 * */
@Composable
public inline fun <reified VM : ViewModel> spikeViewModel(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    entryPoint: ViewModelEntryPoint.Factory = checkNotNull(LocalViewModelEntryPointFactory.current) {
        "No ViewModelEntryPoint was provided via LocalViewModelEntryPointFactory."
    },
    key: String? = null,
    extras: CreationExtras = rememberNavigationExtras(viewModelStoreOwner)
): VM {
    val factory = createSpikeViewModelFactory(viewModelStoreOwner, entryPoint)
    return viewModel(
        viewModelStoreOwner = viewModelStoreOwner,
        key = key,
        factory = factory,
        extras = extras
    )
}

@Composable
@PublishedApi
internal fun createSpikeViewModelFactory(
    viewModelStoreOwner: ViewModelStoreOwner,
    entryPoint: ViewModelEntryPoint.Factory
): ViewModelProvider.Factory? = ViewModelProviderFactory.from(
    owner = viewModelStoreOwner,
    entryPoint = entryPoint
)

/**
 * When using `navigation3` library or generally any other usecase where you'd need to modify [CreationExtras] for you [ViewModel], you may use shit shorthand function.
 * To specifically pass extras to your [ViewModel] via [SavedStateHandle], you should use [args] method in conjuction with this method:
 * ```
 * rememberNavigationExtras {
 *   args { putString("key", value) }
 * }
 * ```
 * */
@Composable
public fun rememberNavigationExtras(
    owner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    setter: MutableCreationExtras.() -> Unit = {},
): CreationExtras {
    val origin = if (owner is HasDefaultViewModelProviderFactory) {
        owner.defaultViewModelCreationExtras
    } else {
        CreationExtras.Empty
    }
    return remember(owner, setter) { MutableCreationExtras(origin).apply(setter) }
}

public inline fun MutableCreationExtras.args(builder: SavedStateWriter.() -> Unit) {
    val args = get(DEFAULT_ARGS_KEY)
    if (args != null) {
        args.write(builder)
    } else {
        set(DEFAULT_ARGS_KEY, savedState(builderAction = builder))
    }
}