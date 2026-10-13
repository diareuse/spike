package spike.compiler

import spike.compiler.harness.Template.Androidx
import spike.compiler.harness.Template.MultiModule
import spike.compiler.harness.Template.Standalone
import spike.compiler.harness.TestHarness
import kotlin.test.Test

class EntryPointTest : TestHarness() {

    @Test
    fun `EntryPoint without companion fails`() = assertBuildFails("no_companion")

    @Test
    fun `missing dependency fails`() = assertBuildFails("missing_dependency")

    @Test
    fun `circular dependency fails`() = assertBuildFails("circular")

    @Test
    fun `EntryPoint must not be class`() = assertBuildFails("klass")

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
    fun `multiple constructors must be marked`() = assertBuildFails("no_marked_constructors")

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
    fun `ktor plugin handles requests`() = assertBuilds("ktor", Standalone, tasks = listOf(":test"))

    @Test
    fun `benchmark compiles`() = assertBuilds("benchmark", tasks = listOf(":compileKotlin"))

    @Test
    fun `singletons are shared within one entry point instance only`() = assertMatchesGolden("singleton")

    @Test
    fun `expect actual gets resolved`() =
        assertMatchesGolden("expect_actual", Standalone, tasks = listOf("assemble", ":jvmRun"))

    @Test
    fun `exports are taken into account`() =
        assertMatchesGolden("multi_module", MultiModule, listOf(":app:run"), modules = listOf("app", "library"))

    @Test
    fun `types with colliding hash codes resolve to their own instances`() = assertBuilds("hash_collision")

    @Test
    fun `two functions providing the same type fail naming both`() =
        assertBuildFails("duplicate_factories", "Duplicate bindings for Cat", "provideTom", "provideFelix")

    @Test
    fun `class and function providing the same type fail naming both`() =
        assertBuildFails(
            "duplicate_class_and_factory",
            "Duplicate bindings for Cat",
            "constructor of Cat",
            "provideFelix"
        )

    @Test
    fun `two classes bound as the same type fail naming both`() =
        assertBuildFails("duplicate_bind_as", "Duplicate bindings for Animal", "Tomcat", "Bulldog")

    @Test
    fun `exports declared in a package are taken into account`() =
        assertBuilds("multi_module_package", MultiModule, tasks = listOf(":app:run"))

    @Test
    fun `plain type resolves to its root binding when multibinding entries share it`() =
        assertBuilds("multibinding_and_root")

    @Test
    fun `multibinding entry injects the root binding, not a sibling entry`() = assertBuilds("multibinding_sibling")

    @Test
    fun `class and function bound to the same map key fail naming both`() =
        assertBuildFails("duplicate_map_key", "Duplicate bindings for", "Cat", "provideFelix")

    @Test
    fun `exported module imports two dependencies sharing a simple name`() =
        assertBuilds("import_name_clash", MultiModule, tasks = listOf(":app:run"))

    @Test
    fun `viewmodels share singletons with each other`() = assertBuilds("androidx_viewmodel_singleton", Androidx)

    @Test
    fun `viewmodel singleton depending on SavedStateHandle fails`() =
        assertBuildFails(
            "androidx_viewmodel_singleton_handle",
            "@Singleton Session depends on",
            "SavedStateHandle",
            template = Androidx
        )

    @Test
    fun `entry points created by one factory each get their own singletons`() = assertBuilds("factory_singleton")

    @Test
    fun `circular dependency error shows exactly the cycle`() =
        assertBuildFails("circular_nested", "Circular dependency detected: Cat -> Dog -> Cat")

    @Test
    fun `multibindings without contributions resolve to empty collections`() = assertBuilds("empty_multibinding")

    @Test
    fun `map requested with a key type no entry uses fails naming the key types`() =
        assertBuildFails("multibinding_key_mismatch", "kotlin.Int", "kotlin.String")

    @Test
    fun `viewmodels share singletons of an exported library`() =
        assertBuilds("androidx_viewmodel_export", MultiModule, tasks = listOf(":app:run"))

    @Test
    fun `viewmodel singleton depending on a Provider of SavedStateHandle fails`() =
        assertBuildFails(
            "androidx_viewmodel_singleton_provider_handle",
            "@Singleton Session depends on",
            template = Androidx
        )

    @Test
    fun `viewmodel reaching a SavedStateHandle singleton through Lazy fails`() =
        assertBuildFails(
            "androidx_viewmodel_lazy_singleton_handle",
            "@Singleton Session depends on",
            template = Androidx
        )

    @Test
    fun `exported module classes sharing one imported dependency compile`() =
        assertBuilds("import_shared", MultiModule, tasks = listOf(":app:run"))

    @Test
    fun `SharesSingletons on a user factory requires opting in to internal API`() = assertBuildFails(
        "shares_singletons_internal",
        "may change without notice. Do not use it directly.",
        task = ":compileKotlin"
    )

}
