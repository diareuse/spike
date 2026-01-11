# Spike

Spike is a Kotlin Multiplatform dependency injection framework that ensures compile-time safety and fast, incremental compilation through KSP. It is designed to be easy to use and universally applicable across all Kotlin platforms.

## Key Features

*   **Kotlin Multiplatform:** Write your dependency injection code once and run it everywhere.
*   **Compile-Time Dependency Resolution:** Avoid runtime errors with compile-time dependency graph validation.
*   **Fast and Incremental:** KSP ensures that your builds remain fast, even as your project grows.
*   **Ease of Use:** A simple and intuitive API makes it easy to get started.
*   **Universality:** Spike is designed to work in any Kotlin project, regardless of platform or framework.

## Usage

### 1. Annotate Your Classes

Spike uses annotations to define your dependency graph. The core annotations are:

*   `@Inject`: Marks a constructor for dependency injection.
*   `@Bind`: Binds an implementation to an interface.
*   `@Include`: Includes a class in the dependency graph.
*   `@Singleton`: Marks a class as a singleton.
*   `@EntryPoint`: Defines an entry point for the dependency graph.

### 2. Define Your Components

Here's an example of how to define your components using the `coffee` example:

```kotlin
// In Heater.kt
interface Heater {
    fun on()
    fun off()
    val isHot: Boolean
}

// In ElectricHeater.kt
@Include
@Bind(Heater::class)
class ElectricHeater(
    private val logger: CoffeeLogger
) : Heater {
    // ...
}

// In CoffeeMaker.kt
@Include
class CoffeeMaker(
    private val logger: CoffeeLogger,
    private val heater: Heater,
    private val pump: Pump
) {
    // ...
}
```

### 3. Create an Entry Point

To access your dependency graph, you need to create an entry point. This is done by creating an interface and annotating it with `@EntryPoint`:

```kotlin
@EntryPoint
interface CoffeeShop {
    fun coffeeMaker(): CoffeeMaker
    companion object
}
```

### 4. Build and Use

After you build your project, Spike will generate an implementation of your entry point. You can then use this to access your dependencies:

```kotlin
val coffeeShop = CoffeeShop() // invoke extension on companion object
val coffeeMaker = coffeeShop.coffeeMaker()
coffeeMaker.brew()
```