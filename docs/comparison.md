# Spike compared with other dependency injection frameworks

Performance is measured on every push to `master` against the same 2000-node graph in every framework: build
times (clean, ABI and non-ABI change), graph creation, resolution, cold start, opening a per-screen component,
and jar size. The numbers live on the [benchmark dashboard](https://diareuse.github.io/spike/dev/bench/), so
this page never goes stale; it compares features.

| | Spike | Dagger / Hilt | Metro | kotlin-inject | Koin |
|---|---|---|---|---|---|
| Graph checked at compile time | yes | yes | yes | yes | with Koin Annotations; the DSL is checked at runtime |
| How | KSP | annotation processor or KSP | Kotlin compiler plugin | KSP | runtime DSL, or KSP with Koin Annotations |
| Kotlin Multiplatform | every target | JVM / Android only | yes | yes | yes |
| Component declaration | `@EntryPoint` interface | `@Component` + modules | `@DependencyGraph` | `@Component` class | `module { }` |
| Bindings | `@Include` on classes, functions, properties | `@Inject`, `@Provides`, `@Binds` | `@Inject`, `@Provides`, `@Binds` | `@Inject`, `@Provides` | `single`, `factory` |
| Scopes and subcomponents | none by design: one entry point instance is one component | yes | yes (graph extensions) | yes (child components) | yes (scopes) |
| Multibindings (Set, List, Map) | yes, merged across modules | Set, Map | Set, Map | Set, Map | via `getAll` |
| Assisted injection | function-type dependency, no annotation | `@AssistedInject` / `@AssistedFactory` | `@AssistedInject` / `@AssistedFactory` | function-type dependency | `parametersOf` |
| Optional bindings | nullable dependency | `@BindsOptionalOf` | nullable / default values | default values | `getOrNull` |
| `Provider` / `Lazy`, breaking cycles | yes | yes | yes | yes | `inject()` |
| Android ViewModels | `@SpikeViewModel`, shared singletons | `@HiltViewModel` | via integrations | manual | `viewModel { }` |
| Test replacements | `@Replaces` + derived test entry point | `@TestInstallIn` | yes | manual | `loadKoinModules` |
| Migration from another framework | converters for JSR-330, Dagger, kotlin-inject and Metro; two-way Koin interop | — | reads Dagger, Hilt, Anvil, kotlin-inject and Guice annotations; graphs interoperate with Dagger and kotlin-inject components | — | JSR-330 annotations with `koin-jsr330` |
| Gradle setup | one plugin, every target | per module | one plugin | one KSP line per target | one dependency |

## Where Spike differs on purpose

- **No scopes.** An entry point instance owns its `@Singleton`s; anything with a shorter life belongs to its own
  entry point or a ViewModel. One concept less to learn, and no scoping errors.
- **Constant-depth resolution.** Spike resolves a graph by walking a flat order instead of nested provider calls,
  so a 2000-deep dependency chain does not overflow the stack.
- **Ambiguity is an error.** Duplicate bindings, two inputs of one type or a map key bound twice fail the build
  and name the declarations involved.

## Moving to Spike

`spike-compiler-jsr`, `spike-compiler-dagger`, `spike-compiler-kotlin-inject` and `spike-compiler-metro` convert
existing declarations and components at build time, so a codebase can move one module at a time.
`spike-compiler-koin` shares dependencies between Spike and Koin in both directions while both run side by side.
