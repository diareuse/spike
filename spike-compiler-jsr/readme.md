# Spike JSR-330

`spike-compiler-jsr` lets classes written for Dagger or another JSR-330 framework join a Spike graph unchanged,
so a codebase can move to Spike one module at a time.

```kotlin
dependencies {
    ksp("io.github.diareuse:spike-compiler:$spike")
    ksp("io.github.diareuse:spike-compiler-jsr:$spike")
    implementation("io.github.diareuse:spike-jsr:$spike")
}
```

For every class with a `javax.inject.Inject` or `jakarta.inject.Inject` constructor, it generates an
`@Include` function that Spike uses like any other binding:

| JSR-330 | Becomes |
|---|---|
| `@Inject constructor` | an `@Include` function calling that constructor |
| `@Singleton` | `@spike.Singleton` |
| `@Named("x")` | `@spike.jsr.Named("x")`; request it with the same annotation in Spike code |
| `Provider<T>` parameter | a `spike.Provider<T>` adapted to the JSR-330 `Provider` |

Field and method injection, generic classes and custom `@Qualifier` annotations are reported as build
errors, because Spike only injects constructors and these cannot be converted mechanically.
