# Spike for Metro

`spike-compiler-metro` turns Metro declarations and dependency graphs into Spike declarations, so an app can
move from Metro to Spike one module at a time. Metro's annotations are read from source; the Metro compiler
plugin is not needed for the converted parts.

```kotlin
dependencies {
    ksp("io.github.diareuse:spike-compiler:$spike")
    ksp("io.github.diareuse:spike-compiler-metro:$spike")
    implementation("io.github.diareuse:spike-metro:$spike")
}
```

| Metro | Becomes |
|---|---|
| `@Inject` / `@AssistedInject` class or constructor | an `@Include` function calling it; `@Assisted` parameters are filled by Spike's function-type dependencies |
| `@SingleIn(...)` or another `@Scope` | `@spike.Singleton` (one instance per entry point instance) |
| `@Provides` in an object, a companion object or a stateless interface | an `@Include` function |
| `@Binds val Impl.bind: Api` | an `@Include` function returning the receiver as `Api` |
| `@ContributesBinding` / `@ContributesIntoSet` to a single supertype | an `@Include` binding (scopes are ignored: Spike has one graph) |
| `@IntoSet`, `@ElementsIntoSet`, `@IntoMap` with `@StringKey`, `@IntKey`, `@ClassKey` | Spike multibindings with the `spike.metro` keys |
| `@Named` | `@spike.metro.Named` |
| `@AssistedFactory fun interface Factory { fun create(id: String): Detail }` | an implementation built from Spike's `(String) -> Detail` |
| `@DependencyGraph interface AppGraph` | `@EntryPoint interface AppGraphSpike` with its accessors, created by `AppGraphSpike.factory().create(...)` |

Reported as build errors: graph extensions, `@Includes` inputs, `@Provides` in a graph body (move it into the
companion object), contributions with an explicit `binding` or several supertypes, Metro's own `Provider`
parameters (use `spike.Provider` or `Lazy`), `@OptionalBinding` and `@DefaultBinding` (Spike passes `null` for an
unbound nullable type), and custom qualifiers. `@Multibinds` needs no conversion: an uncontributed multibinding is
empty in Spike.
