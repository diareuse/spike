# Spike

Spike is a Kotlin Multiplatform dependency injection compositor that ensures compile-time safety and fast, incremental compilation through KSP. It is designed to be straightforward to use and universally applicable across all Kotlin platforms.

[![Static Badge](https://img.shields.io/badge/build-benchmark-blue)](https://diareuse.github.io/spike/dev/bench/)
[![Static Badge](https://img.shields.io/badge/changelog-691337)](CHANGELOG.md)
[![GitHub Tag](https://img.shields.io/github/v/tag/diareuse/spike)](https://github.com/diareuse/spike/releases)

### `gradle/libs.versions.toml`

```toml
[versions]
spike = "<see 'tag' above>"

[libraries]
spike = { module = "io.github.diareuse:spike", version.ref = "spike" }
spike-ktor = { module = "io.github.diareuse:spike-ktor", version.ref = "spike" }
spike-compiler = { module = "io.github.diareuse:spike-compiler", version.ref = "spike" }
spike-androidx = { module = "io.github.diareuse:spike-androidx", version.ref = "spike" }
spike-androidx-compose = { module = "io.github.diareuse:spike-androidx-compose", version.ref = "spike" }
```

--- or ---

### `app/build.gradle.kts` (Android)

```kotlin
dependencies {
    val spike = "see 'tag' above"
    ksp("io.github.diareuse:spike-compiler:$spike")
    implementation("io.github.diareuse:spike:$spike")
    implementation("io.github.diareuse:spike-ktor:$spike")
    implementation("io.github.diareuse:spike-androidx:$spike")
    implementation("io.github.diareuse:spike-androidx-compose:$spike")
}
```

### `shared/build.gradle.kts` (KMP)

```kotlin
val spike = "see 'tag' above"
kotlin {
    // …
    sourceSets {
        androidMain.dependencies {
            // if not using jetpack compose
            implementation("io.github.diareuse:spike-androidx:$spike")
        }
        commonMain.dependencies {
            implementation("io.github.diareuse:spike:$spike")
            // if using jetpack compose
            implementation("io.github.diareuse:spike-androidx-compose:$spike")
        }
    }
}
dependencies {
    add("kspAndroid", "io.github.diareuse:spike-compiler:$spike")
    add("kspJs", "io.github.diareuse:spike-compiler:$spike")
    add("kspWasmJs", "io.github.diareuse:spike-compiler:$spike")
    add("kspJvm", "io.github.diareuse:spike-compiler:$spike")
    // and all other targets…
}
```

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

* `@Include`: Includes a class, a provider function or a provider property in the dependency graph. Functions and properties must be top level or inside an object, such as a companion object.
* `@Include.Constructor`: Marks the constructor Spike uses when an `@Include` class has more than one.
* `@Singleton`: Marks a class as a singleton (only instantiated once per entry point instance; two entry point instances never share a singleton).
* `@EntryPoint`: Defines an entry point interface for the dependency graph.

### 2. Define Your Components

Here's an example of how to define your components:

```kotlin
interface Heater

@Include
class ElectricHeater : Heater

// Bind a class to an interface with an @Include extension property or function
@Include
val ElectricHeater.asHeater: Heater get() = this

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
    // If a class has multiple constructors, annotate one with @Include.Constructor
    @Include.Constructor
    constructor()

    constructor(capacity: Int)
}

// Mark a class with @Singleton to share the same instance within an entry point
@Singleton
@Include
class ShopInventory

// Depend on a function type to pass some values at runtime: Spike builds Order with the
// call's arguments and takes everything else from the graph
@Include
class Order(val table: Int, val inventory: ShopInventory)

@Include
class Waiter(private val order: (Int) -> Order) {
    fun serve(table: Int) = order(table)
}
```

Each argument type of a function-type dependency must be distinct and must not also be bound in the graph.
A `@Singleton`, `Lazy` or `Provider` cannot depend on an argument, because it would outlive the call.

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

## License

Spike is licensed under the [Mozilla Public License 2.0](LICENSE). Changes to Spike's own files must be
shared under the same license; the code you write with Spike can be under any license, open or closed.

Code that the Spike compiler generates into your project (for example `*_Factory`, `*_Program0`,
`*_EntryPoint`) is not covered by the MPL-2.0. It belongs to you and you may license it however you like.

Releases up to and including 0.0.15 were published under GPL-v3.
