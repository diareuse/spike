package spike.compiler

import spike.compiler.harness.CompilerHarness
import spike.compiler.harness.Libraries.Androidx
import kotlin.test.Test

class CompilerTest : CompilerHarness() {

    @Test
    fun `EntryPoint without companion fails`() = assertProcessingFails("no_companion")

    @Test
    fun `missing dependency fails`() = assertProcessingFails("missing_dependency")

    @Test
    fun `circular dependency fails`() = assertProcessingFails("circular")

    @Test
    fun `EntryPoint must not be class`() = assertProcessingFails("klass")

    @Test
    fun `standalone EntryPoint with properties`() = assertMatchesGolden("entrypoint")

    @Test
    fun `EntryPoint factory is created`() = assertMatchesGolden("entry_factory")

    @Test
    fun `complex tree is resolved`() = assertMatchesGolden("complex")

    @Test
    fun `binds interfaces to concrete classes`() = assertMatchesGolden("bind")

    @Test
    fun `EntryPoint generates providers`() = assertMatchesGolden("provider")

    @Test
    fun `entries with qualifiers resolve`() = assertMatchesGolden("qualifier")

    @Test
    fun `multiple constructors must be marked`() = assertProcessingFails("no_marked_constructors")

    @Test
    fun `multiple constructors are marked`() = assertMatchesGolden("marked_constructors")

    @Test
    fun `multi binds are possible`() = assertMatchesGolden("multibinding")

    @Test
    fun `multi binds work with tuple-generics`() = assertMatchesGolden("multibinding_interfaces")

    @Test
    fun `objects are composed`() = assertMatchesGolden("compositor")

    @Test
    fun `out variance is preserved`() = assertMatchesGolden("out_variance")

    @Test
    fun `androidx viewmodels are found`() = assertMatchesGolden("androidx_viewmodel", Androidx)

    @Test
    fun `benchmark compiles`() = assertCompiles("benchmark")

    @Test
    fun `singletons are shared within one entry point instance only`() = assertMatchesGolden("singleton")

    @Test
    fun `types with colliding hash codes resolve to their own instances`() = assertRuns("hash_collision")

    @Test
    fun `two functions providing the same type fail naming both`() =
        assertProcessingFails("duplicate_factories", "Duplicate bindings for Cat", "provideTom", "provideFelix")

    @Test
    fun `class and function providing the same type fail naming both`() = assertProcessingFails(
        "duplicate_class_and_factory",
        "Duplicate bindings for Cat",
        "constructor of Cat",
        "provideFelix"
    )

    @Test
    fun `two classes bound as the same type fail naming both`() =
        assertProcessingFails("duplicate_bind_as", "Duplicate bindings for Animal", "Tomcat", "Bulldog")

    @Test
    fun `exports declared in a package are taken into account`() = assertRuns("multi_module_package")

    @Test
    fun `plain type resolves to its root binding when multibinding entries share it`() =
        assertRuns("multibinding_and_root")

    @Test
    fun `multibinding entry injects the root binding, not a sibling entry`() = assertRuns("multibinding_sibling")

    @Test
    fun `class and function bound to the same map key fail naming both`() =
        assertProcessingFails("duplicate_map_key", "Duplicate bindings for", "Cat", "provideFelix")

    @Test
    fun `exported module imports two dependencies sharing a simple name`() = assertRuns("import_name_clash")

    @Test
    fun `viewmodels share singletons with each other`() = assertRuns("androidx_viewmodel_singleton", Androidx)

    @Test
    fun `viewmodel singleton depending on SavedStateHandle fails`() = assertProcessingFails(
        "androidx_viewmodel_singleton_handle",
        "@Singleton Session depends on",
        "SavedStateHandle",
        libraries = Androidx
    )

    @Test
    fun `entry points created by one factory each get their own singletons`() = assertRuns("factory_singleton")

    @Test
    fun `circular dependency error shows exactly the cycle`() =
        assertProcessingFails("circular_nested", "Circular dependency detected: Cat -> Dog -> Cat")

    @Test
    fun `multibindings without contributions resolve to empty collections`() = assertRuns("empty_multibinding")

    @Test
    fun `map requested with a key type no entry uses fails naming the key types`() =
        assertProcessingFails("multibinding_key_mismatch", "kotlin.Int", "kotlin.String")

    @Test
    fun `viewmodel singleton depending on a Provider of SavedStateHandle fails`() = assertProcessingFails(
        "androidx_viewmodel_singleton_provider_handle",
        "@Singleton Session depends on",
        libraries = Androidx
    )

    @Test
    fun `viewmodel reaching a SavedStateHandle singleton through Lazy fails`() = assertProcessingFails(
        "androidx_viewmodel_lazy_singleton_handle",
        "@Singleton Session depends on",
        libraries = Androidx
    )

    @Test
    fun `viewmodels share singletons of an exported library`() = assertRuns("androidx_viewmodel_export", Androidx)

    @Test
    fun `exported module classes sharing one imported dependency compile`() = assertRuns("import_shared")

    @Test
    fun `SharesSingletons on a user factory requires opting in to internal API`() =
        assertCompilationFails("shares_singletons_internal", "may change without notice. Do not use it directly.")

}
