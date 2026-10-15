package spike.compiler

import spike.compiler.harness.HarnessCompiler
import spike.compiler.harness.Libraries.Androidx
import org.junit.jupiter.api.Timeout
import kotlin.test.Test

class CompilerTest : HarnessCompiler() {

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
    fun `a factory parameter that is also an Include binding fails naming both`() =
        assertProcessingFails(
            "factory_parameter_shadows_include",
            "kotlin.String is both an input of the entry point and an @Include binding",
            "parameter config of Configured.Factory.create",
            "function defaultConfig"
        )

    @Test
    fun `an exported dependency that is also an Include binding fails naming both`() =
        assertProcessingFails(
            "external_shadows_include",
            "Lib is both an input of the entry point and an @Include binding",
            "property lib of @Export Exported",
            "function otherLib"
        )

    @Test
    fun `two factory parameters of one type fail naming both`() = assertProcessingFails(
        "two_factory_parameters_same_type",
        "kotlin.String is an input of the entry point more than once",
        "parameter primary of Configured.Factory.create",
        "parameter secondary of Configured.Factory.create"
    )

    @Test
    fun `a factory parameter of an exported type fails naming both`() = assertProcessingFails(
        "parameter_and_export_same_type",
        "Lib is an input of the entry point more than once",
        "parameter lib of PropertyEntryPoint.Factory.create",
        "property lib of @Export Exported"
    )

    @Test
    fun `two inputs of one type compile while nothing requests that type`() = assertRuns("repeated_input_unrequested")

    @Test
    fun `qualified factory parameters of one type each reach their own consumer`() =
        assertRuns("qualified_parameters_of_one_type")

    @Test
    fun `a multibinding entry of a repeated input's type contributes its own value`() =
        assertRuns("multibinding_entry_of_repeated_input_type")

    @Test
    fun `an export exposing one type twice fails when that type is requested`() = assertProcessingFails(
        "export_property_and_function_same_type",
        "Lib is an input of the entry point more than once",
        "property lib of @Export Exported",
        "function otherLib of @Export Exported"
    )

    @Test
    fun `a type exported only through a function reaches the consuming module`() = assertRuns("export_function")

    @Test
    fun `a qualifier declared in a library qualifies types in the consuming module`() =
        assertRuns("qualifier_from_library")

    @Test
    fun `qualified exports of one type each reach their own consumer`() = assertRuns("export_qualified_members")

    @Test
    fun `a member exported by an interface and its exported subinterface fails naming both`() = assertProcessingFails(
        "export_inherited_members",
        "Lib is an input of the entry point more than once",
        "property lib of @Export Base",
        "property lib of @Export Derived"
    )

    @Test
    fun `a multibinding entry of an input's type contributes its own value`() =
        assertRuns("multibinding_entry_of_input_type")

    @Test
    fun `a qualified input and an unqualified Include binding of one type both resolve`() =
        assertRuns("qualified_input_beside_include")

    @Test
    fun `an Include extension property binds its receiver to the property type`() =
        assertRuns("bind_extension_property")

    @Test
    fun `an Include extension function binds its receiver to the return type`() =
        assertRuns("bind_extension_function")

    @Test
    fun `an Include function taking the implementation binds it to the return type`() =
        assertRuns("bind_function_parameter")

    @Test
    fun `an Include top-level property provides its type`() = assertRuns("include_property")

    @Test
    fun `an Include extension property can contribute to a multibinding`() =
        assertRuns("bind_extension_property_multibinding")

    @Test
    fun `an Include function in a companion object binds its parameter to the return type`() =
        assertRuns("bind_companion_function")

    @Test
    fun `an Include extension property in a companion object binds its receiver`() =
        assertRuns("bind_companion_extension_property")

    @Test
    fun `an Include extension function in a companion object binds its receiver`() =
        assertRuns("bind_companion_extension_function")

    @Test
    fun `an Include member of a class that is not an object fails`() = assertProcessingFails(
        "include_member_of_class",
        "@Include property asCat is declared in class Bindings",
        "top level or inside an object"
    )

    @Test
    fun `an Include member function of a class that is not an object fails`() = assertProcessingFails(
        "include_member_function_of_class",
        "@Include function cat is declared in class Bindings",
        "top level or inside an object"
    )

    @Test
    fun `a class and extension properties providing the same type fail naming each`() = assertProcessingFails(
        "duplicate_class_and_extension_property",
        "Duplicate bindings for Cat",
        "property Cat.Companion.asCat on HouseCat",
        "property asCat on StrayCat"
    )

    @Test
    fun `a Singleton Include property is evaluated once per entry point`() = assertRuns("include_singleton_property")

    @Test
    fun `a qualified Include class resolves only for the qualified type`() = assertRuns("include_qualified_class")

    @Test
    fun `a qualified Include property resolves only for the qualified type`() =
        assertRuns("include_qualified_property")

    @Test
    fun `an Include extension property can contribute to a keyed map`() = assertRuns("bind_extension_property_map")

    @Test
    fun `an Include property with a backing field fails`() = assertProcessingFails(
        "include_property_with_backing_field",
        "@Include property clock has a backing field",
        "val clock get() = "
    )

    @Test
    fun `Include with bindAs on a qualified multibinding entry keeps bindTo, key and qualifier in the replacement`() =
        assertProcessingFails(
            "bind_as_removed_multibinding",
            "bindAs was removed from @Include",
            "@spike.Include(bindTo = spike.BindTarget.Map)",
            "@StringKey(\"logging\")",
            "@Kind(\"core\")",
            "val LoggingPlugin.asPlugin: Plugin get() = this"
        )

    @Test
    fun `Include with bindAs on a function suggests returning the bound type`() = assertProcessingFails(
        "bind_as_removed_function",
        "bindAs was removed from @Include",
        "return Cat from sphynx",
        "fun sphynx(): Cat"
    )

    @Test
    fun `Include with bindAs on a property suggests declaring the bound type`() = assertProcessingFails(
        "bind_as_removed_property",
        "bindAs was removed from @Include, declare it as Cat",
        "val sphynx: Cat get() = ..."
    )

    @Test
    fun `Include with bindAs fails explaining the replacement`() = assertProcessingFails(
        "bind_as_removed",
        "bindAs was removed from @Include",
        "val HouseCat.asCat: Cat get() = this"
    )

    @Test
    fun `null passed for a nullable factory parameter reaches every consumer`() =
        assertRuns("nullable_factory_parameter")

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
    fun `large graphs generate only methods small enough for the JIT to compile`() =
        assertMethodsFitJit("large_graph")

    @Test
    fun `a dependency chain thousands deep resolves on a small thread stack`() = assertRuns("deep_chain")

    @Test
    fun `an entry point exposing a Provider builds nothing until it is called`() = assertRuns("provider_root")

    @Test
    fun `values shared across a large program reach every consumer`() = assertRuns("split_program")

    @Test
    fun `entry point members may share names with inputs and generated internals`() = assertRuns("member_names")

    @Test
    @Timeout(60)
    fun `a graph whose dependencies are shared along many paths compiles without expanding every path`() = assertRuns("shared_lattice")

    @Test
    fun `SharesSingletons on a user factory requires opting in to internal API`() =
        assertCompilationFails("shares_singletons_internal", "may change without notice. Do not use it directly.")

}
