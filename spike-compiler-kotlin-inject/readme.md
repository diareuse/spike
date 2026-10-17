# Spike for kotlin-inject

`spike-compiler-kotlin-inject` turns kotlin-inject declarations and components into Spike declarations, so an
app can move to Spike one module at a time.

```kotlin
dependencies {
    ksp("io.github.diareuse:spike-compiler:$spike")
    ksp("io.github.diareuse:spike-compiler-kotlin-inject:$spike")
}
```

| kotlin-inject | Becomes |
|---|---|
| `@Inject` class or constructor | an `@Include` function calling it |
| a `@Scope` annotation | `@spike.Singleton` (one instance per entry point instance) |
| `@Assisted` parameters | plain parameters, filled by Spike's function-type dependencies such as `(String) -> Detail` |
| `@Provides` in an object or a stateless interface, including `fun Impl.bind(): Api = this` | an `@Include` function |
| `@IntoSet` | `@Include(bindTo = BindTarget.Set)` |
| `@Component abstract class AppComponent(@get:Provides val user: User)` | `@EntryPoint interface AppComponentSpike` with the same abstract members, created by `AppComponentSpike.factory().create(user)` |

Reported as build errors: `@IntoMap` (its keys are built at runtime), `@Provides` inside a component class,
parent components (Spike has no component hierarchy), generic or private `@Inject` classes and kotlin-inject
qualifiers.
