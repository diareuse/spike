# Spike

Spike is a Kotlin Multiplatform dependency injection compositor that ensures compile-time safety and fast, incremental compilation through KSP. It is designed to be straightforward to use and universally applicable across all Kotlin platforms.

[![Static Badge](https://img.shields.io/badge/build-benchmark-blue)](https://diareuse.github.io/spike/dev/bench/)

## Integrations

Spike offers specific integrations to simplify its usage across various frameworks. Check out their
respective documentation for more details:

* [Spike AndroidX](spike-androidx/readme.md) (`spike-androidx`) - Support for Multiplatform AndroidX
  ViewModels.
* [Spike AndroidX Compose](spike-androidx-compose/readme.md) (`spike-androidx-compose`) - Jetpack
  Compose integration for Spike ViewModels.
* [Spike Ktor](spike-ktor/readme.md) (`spike-ktor`) - Seamless dependency injection for Ktor
  applications.

## Key Features

*   **Kotlin Multiplatform:** Write your dependency injection code once and run it everywhere.
*   **Compile-Time Dependency Resolution:** Avoid runtime errors with compile-time dependency graph validation.
*   **Fast and Incremental:** KSP ensures that your builds remain fast, even as your project grows.
*   **Ease of Use:** A simple and intuitive API makes it easy to get started.
*   **Universality:** Spike is designed to work in any Kotlin project, regardless of platform or framework.

## Usage

### 1. Annotate Your Classes

Spike uses annotations to define your dependency graph. The core annotations are:

* `@Include`: Includes a class or a provider function in the dependency graph.
* `@Inject`: Explicitly marks a constructor for dependency injection when multiple constructors are
  present.
* `@Singleton`: Marks a class as a singleton (only instantiated once per entry point).
* `@EntryPoint`: Defines an entry point interface for the dependency graph.

### 2. Define Your Components

Here's an example of how to define your components:

```kotlin
interface Heater

// Use bindAs to bind a class to an interface
@Include(bindAs = Heater::class)
class ElectricHeater : Heater

// Spike can inject standard `Lazy<T>` or `spike.Provider<T>` automatically
@Include
class CoffeeMaker(
    private val heater: Heater,
    private val pumpProvider: spike.Provider<Pump>,
    private val logger: Lazy<CoffeeLogger>
) {
    fun brew() {
        val pump = pumpProvider.get()
        // ...
    }
}

// You can also use @Include on functions to provide external or third-party classes!
@Include
fun provideCoffeeLogger(): CoffeeLogger = ConsoleCoffeeLogger()

@Include
class Pump {
    // If a class has multiple constructors, annotate one with @Inject
    @Inject
    constructor()

    constructor(capacity: Int)
}

// Mark a class with @Singleton to share the same instance within an entry point
@Singleton
@Include
class ShopInventory
```

### 3. Create an Entry Point

To access your dependency graph, you need to create an interface and annotate it with `@EntryPoint`.
A companion object is required so Spike can generate an extension function on it.

```kotlin
@EntryPoint
interface CoffeeShop {
    val coffeeMaker: CoffeeMaker
    val inventory: ShopInventory

    companion object
}
```

### 4. Build and Use

After building your project, Spike generates a factory extension on your entry point's companion
object. You can use it to create your graph and access the dependencies:

```kotlin
fun main() {
    val coffeeShop = CoffeeShop() // invokes generated extension on CoffeeShop.Companion
    val coffeeMaker = coffeeShop.coffeeMaker
    coffeeMaker.brew()
}
```
