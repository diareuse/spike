package spike.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import spike.lifecycle.viewmodel.ViewModelEntryPoint

/**
 * This provider must be filled with a generated factory. This factory, however, cannot be inferred at build-time of
 * this library due to obvious reasons. Therefore, you must provide it by calling:
 *
 * ```
 * CompositionLocalProvider(
 *   LocalViewModelEntryPointFactory provides ViewModelEntryPoint.factory
 * ) {
 *   // … your composition
 * }
 * ```
 * */
object LocalViewModelEntryPointFactory {
    private val LocalViewModelEntryPointFactory = staticCompositionLocalOf<ViewModelEntryPoint.Factory?> {
        null
    }
    val current
        @Composable get() = LocalViewModelEntryPointFactory.current

    infix fun provides(factory: ViewModelEntryPoint.Factory) = LocalViewModelEntryPointFactory provides factory
}
