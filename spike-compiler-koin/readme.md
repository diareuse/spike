# Spike for Koin

`spike-compiler-koin` lets Spike and Koin share dependencies in one app, in both directions.

```kotlin
dependencies {
    ksp("io.github.diareuse:spike-compiler:$spike")
    ksp("io.github.diareuse:spike-compiler-koin:$spike")
    implementation("io.github.diareuse:spike-koin:$spike")
}
```

## Spike objects in Koin

Every `@EntryPoint` gets a generated `koinModule()` that exposes each of its members as a Koin `factory`:

```kotlin
val app = App()
startKoin { modules(app.koinModule()) }
// get<Repository>() now returns app.repository
```

Members with a Spike qualifier are left out.

## Koin objects in Spike

List the types an entry point takes from Koin with `@FromKoin`, and pass the `Koin` instance as an input:

```kotlin
@FromKoin(Clock::class)
@EntryPoint
interface App {
    val repository: Repository

    @EntryPoint.Factory
    interface Factory {
        fun create(koin: Koin): App
    }

    companion object
}

val app = App.factory().create(getKoin())
```

Spike checks everything else at compile time; a type listed in `@FromKoin` is looked up in Koin when it is
needed, so a missing Koin definition fails at runtime, as it would in Koin.
