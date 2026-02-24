# Spike AndroidX Compose

`spike-androidx-compose` provides Jetpack Compose and Compose Multiplatform integration for [Spike](../spike-androidx) ViewModels. It allows you to easily retrieve injected ViewModels within your `@Composable` functions using an API similar to `hiltViewModel()`.

## Features

- **Compose-First API**: Use `spikeViewModel<VM>()` to get your injected ViewModels.
- **SavedState Support**: Automatically handles `SavedStateHandle` injection.
- **CompositionLocal Integration**: Uses `LocalViewModelEntryPointFactory` to provide the dependency graph to your composables.
- **Multiplatform**: Supports Android, iOS, and other Compose-compatible targets.

## Setup

### 1. Provide the ViewModel Factory

In your root-level composable (e.g., in your Activity's `setContent` or a Main screen), you must provide the generated `ViewModelEntryPoint.Factory` via `LocalViewModelEntryPointFactory`.

Spike generates a `factory` extension property on the `ViewModelEntryPoint` companion object.

```kotlin
import androidx.compose.runtime.CompositionLocalProvider
import spike.compose.LocalViewModelEntryPointFactory
import spike.lifecycle.viewmodel.ViewModelEntryPoint
import spike.lifecycle.viewmodel.factory // Generated extension property

@Composable
fun MyApp() {
    CompositionLocalProvider(
        LocalViewModelEntryPointFactory provides ViewModelEntryPoint.factory
    ) {
        // Your navigation and screens
        MyScreen()
    }
}
```

## Usage

### Using `spikeViewModel()`

Inside any `@Composable` function that is a descendant of the provider above, you can call `spikeViewModel()` to retrieve an instance of your `@SpikeViewModel`.

```kotlin
import androidx.compose.runtime.Composable
import spike.lifecycle.viewmodel.compose.spikeViewModel

@Composable
fun MyScreen() {
    // Automatically injected with its dependencies and SavedStateHandle
    val viewModel: MyViewModel = spikeViewModel()
    
    // ... use your viewModel
}
```

For more advanced use cases, you can also specify a `key`, a different `ViewModelStoreOwner`, or even a custom `ViewModelEntryPoint.Factory`:

```kotlin
val viewModel: MyViewModel = spikeViewModel(
    key = "custom_key",
    viewModelStoreOwner = customOwner,
    entryPoint = customEntryPointFactory
)
```

### Comparison with `hiltViewModel()`

If you are coming from Dagger/Hilt, `spikeViewModel()` is the direct equivalent of `hiltViewModel()`.

| Feature | Hilt | Spike |
| :--- | :--- | :--- |
| **Annotation** | `@HiltViewModel` | `@SpikeViewModel` |
| **Injection** | `hiltViewModel()` | `spikeViewModel()` |
| **Setup** | `@AndroidEntryPoint` | `LocalViewModelEntryPointFactory` |

## How it Works

`spikeViewModel()` uses `LocalViewModelEntryPointFactory` to retrieve the `ViewModelEntryPoint.Factory`. It then creates a `ViewModelProvider.Factory` that knows how to resolve ViewModels using Spike's generated dependency graph.

It automatically integrates with the `LocalViewModelStoreOwner` and provides the `SavedStateHandle` to the ViewModel constructor if it's requested.
