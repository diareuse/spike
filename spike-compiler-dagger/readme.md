# Spike for Dagger

`spike-compiler-dagger` turns Dagger modules, multibindings and components into Spike declarations, so an
app can move from Dagger to Spike one module at a time. It includes `spike-compiler-jsr`, so `@Inject`
constructors are converted too.

```kotlin
dependencies {
    ksp("io.github.diareuse:spike-compiler:$spike")
    ksp("io.github.diareuse:spike-compiler-dagger:$spike")
    implementation("io.github.diareuse:spike-jsr:$spike")
    implementation("io.github.diareuse:spike-dagger:$spike")
}
```

| Dagger | Becomes |
|---|---|
| `@Provides` in an object, a companion or a module class without constructor parameters | an `@Include` function calling it |
| `@Binds abstract fun bind(impl: Impl): Api` | an `@Include` function returning `impl` as `Api` |
| `@Singleton`, any other `@Scope`, `@Reusable` | `@spike.Singleton` (one instance per entry point instance) |
| `@IntoSet`, `@ElementsIntoSet` | `@Include(bindTo = BindTarget.Set)` |
| `@IntoMap` with `@StringKey`, `@IntKey`, `@LongKey`, `@ClassKey` | `@Include(bindTo = BindTarget.Map)` with the `spike.dagger` key |
| `@Named` | `@spike.jsr.Named` |
| `Provider<T>`, `dagger.Lazy<T>` parameters | `spike.Provider<T>`, `Lazy<T>`, adapted back |
| `@Component interface AppComponent` | `@EntryPoint interface AppComponentSpike : AppComponent` |
| `@Component.Factory` with `@BindsInstance` | `AppComponentSpike.Factory`, an `@EntryPoint.Factory` |

Replace `DaggerAppComponent.factory().create(...)` with `AppComponentSpike.factory().create(...)`.

Not converted, reported as build errors: `@Component.Builder`, modules with constructor parameters,
custom `@MapKey` and custom `@Qualifier` annotations. Subcomponents and members-injection methods are not
supported by Spike. Hilt is tracked separately.
