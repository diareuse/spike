# Spike AndroidX

`spike-androidx` provides seamless integration for AndroidX ViewModels within the Spike dependency injection framework. It allows you to inject dependencies into your ViewModels and automatically manage their lifecycle using standard AndroidX Lifecycle APIs.

This library is built for **Kotlin Multiplatform**, allowing you to share ViewModel logic across Android, iOS, and other supported platforms.

## Features

- **Automatic Discovery**: Annotate your `ViewModel` with `@SpikeViewModel` to include it in the graph.
- **SavedStateHandle Support**: `SavedStateHandle` is automatically available for injection into your ViewModels on Android.
- **Standard API Integration**: Works with standard `by viewModels()` and `ViewModelProvider` delegated properties.
- **Multiplatform Ready**: Leverages the official AndroidX Lifecycle Multiplatform library.

## Getting Started

### 1. Define your ViewModel

Annotate your `ViewModel` class with `@SpikeViewModel`. You can inject any dependencies registered in your Spike graph.

```kotlin
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import spike.lifecycle.viewmodel.SpikeViewModel

@SpikeViewModel
class UserProfileViewModel(
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle // Automatically provided
) : ViewModel() {
    val userId: String = savedStateHandle["userId"] ?: throw IllegalArgumentException("userId is required")
    
    // ...
}
```

### 2. Integration in Activity or Fragment (Android)

To enable Spike to provide your ViewModels, you must override the `defaultViewModelProviderFactory` in your Activity or Fragment. Spike generates an extension property `factory` on the `ViewModelEntryPoint` companion object.

```kotlin
import androidx.activity.viewModels
import spike.lifecycle.viewmodel.ViewModelEntryPoint
import spike.lifecycle.viewmodel.ViewModelProviderFactory
import spike.lifecycle.viewmodel.factory // Generated extension property

class UserProfileActivity : AppCompatActivity() {

    // Override the default factory to use Spike's generated entry point
    override val defaultViewModelProviderFactory by lazy {
        ViewModelProviderFactory.from(this, ViewModelEntryPoint.factory)!!
    }

    // Use the standard by viewModels() delegate
    private val viewModel: UserProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ...
    }
}
```

## How it works

Spike's compiler scans for classes annotated with `@SpikeViewModel` and includes them in a generated implementation of `ViewModelEntryPoint`. 

When `ViewModelProviderFactory` is used:
1. It retrieves the `SavedStateHandle` from the `CreationExtras`.
2. It uses the `ViewModelEntryPoint.Factory` to create an entry point scoped with that `SavedStateHandle`.
3. It looks up the requested `ViewModel` in the entry point's `viewModels` map and returns the injected instance.

## Compose Support

If you are using Jetpack Compose or Compose Multiplatform, check out `spike-androidx-compose` for even simpler integration:

```kotlin
@Composable
fun UserProfileScreen() {
    // Uses LocalViewModelEntryPointFactory under the hood
    val viewModel: UserProfileViewModel = spikeViewModel()
    // ...
}
```
