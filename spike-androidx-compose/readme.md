# Spike AndroidX Compose

`spike-androidx-compose` provides a Compose integration for Spike ViewModels. It lets you retrieve
injected ViewModels inside `@Composable` functions with an API similar to `viewModel()` and
`hiltViewModel()`.

## Features

- **Compose-first API**: Use `spikeViewModel<VM>()` to get a ViewModel from Spike.
- **Injected ViewModel resolution**: Resolves ViewModels from a `ViewModelEntryPoint.Factory`.
- **SavedState support**: Uses `CreationExtras` and `SavedStateHandle` support when available.
- **CompositionLocal integration**: Reads the entry-point factory from
  `LocalViewModelEntryPointFactory`.
- **Fallback support**: If Spike does not provide a ViewModel binding, it falls back to the owner’s
  default `ViewModelProvider.Factory` when the owner supports it.
- **Multiplatform-friendly**: Works with Compose targets that provide a `ViewModelStoreOwner`.

## Setup

### Provide the entry-point factory

You must provide the generated `ViewModelEntryPoint.Factory` through
`LocalViewModelEntryPointFactory` before calling `spikeViewModel()`.

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import spike.compose.LocalViewModelEntryPointFactory
import spike.lifecycle.viewmodel.ViewModelEntryPoint

@Composable
fun MyApp() {
    CompositionLocalProvider(
        LocalViewModelEntryPointFactory provides ViewModelEntryPoint.factory
    ) {
        MyScreen()
    }
}
```

## Usage

### Get a ViewModel

Inside any `@Composable` function below the provider, call `spikeViewModel()`.

```kotlin
import androidx.compose.runtime.Composable
import spike.lifecycle.viewmodel.compose.spikeViewModel

@Composable
fun MyScreen() {
    val viewModel: MyViewModel = spikeViewModel()
}
```

You can also provide a custom key, `ViewModelStoreOwner`, entry point, or creation extras.

```kotlin
val viewModel: MyViewModel = spikeViewModel(
    key = "custom_key",
    viewModelStoreOwner = customOwner,
    entryPoint = customEntryPointFactory,
    extras = customExtras
)
```

### Pass values into `SavedStateHandle`

If your ViewModel reads values from `SavedStateHandle`, use `rememberNavigationExtras()` and
`args()`.

```kotlin
import androidx.compose.runtime.Composable
import spike.lifecycle.viewmodel.compose.args
import spike.lifecycle.viewmodel.compose.rememberNavigationExtras
import spike.lifecycle.viewmodel.compose.spikeViewModel

@Composable
fun MyScreen(userId: String) {
    val viewModel: MyViewModel = spikeViewModel(
        extras = rememberNavigationExtras {
            args {
                putString("userId", userId)
                putInt("screenId", 42)
            }
        }
    )
}
```

## How it works

`spikeViewModel()` reads:

- the current `ViewModelStoreOwner` from `LocalViewModelStoreOwner`
- the current Spike entry-point factory from `LocalViewModelEntryPointFactory`

It then builds a `ViewModelProvider.Factory` through `ViewModelProviderFactory.from(...)`.

That factory:

- asks the Spike entry point for a provider of the requested ViewModel,
- creates a `SavedStateHandle` from the supplied `CreationExtras`,
- falls back to the owner’s default `ViewModelProvider.Factory` if Spike does not provide the
  requested ViewModel.

If no `ViewModelStoreOwner` is available, `spikeViewModel()` fails fast.

If no `ViewModelEntryPoint.Factory` is provided, it also fails fast.

## Using Spike with `by viewModels()`

If you want Spike ViewModels to work with the standard `by viewModels()` API, provide Spike’s
factory as the default ViewModel factory on an owner that supports it.

```kotlin
override val defaultViewModelProviderFactory by lazy {
    ViewModelProviderFactory.from(this, ViewModelEntryPoint.factory)
}
```

Then you can use:

```kotlin
val viewModel: MyViewModel by viewModels()
```

## Comparison with `hiltViewModel()`

If you are coming from Hilt, `spikeViewModel()` is the closest equivalent.

| Feature     | Hilt                 | Spike                             |
|:------------|:---------------------|:----------------------------------|
| Annotation  | `@HiltViewModel`     | `@SpikeViewModel`                 |
| Compose API | `hiltViewModel()`    | `spikeViewModel()`                |
| Root setup  | `@AndroidEntryPoint` | `LocalViewModelEntryPointFactory` |

## Notes

- `LocalViewModelEntryPointFactory` must be provided before calling `spikeViewModel()`.
- `spikeViewModel()` requires a valid `ViewModelStoreOwner`.
- `ViewModelProviderFactory.from(...)` only works with owners that implement
  `HasDefaultViewModelProviderFactory`.
- `rememberNavigationExtras()` can be used to customize `CreationExtras` before ViewModel creation.
- `args()` only writes into `SavedStateHandle` extras when a default args bundle is present, and
  otherwise initializes it from the provided builder.
