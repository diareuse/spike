package spike.compiler

import spike.compiler.harness.HarnessGradle
import kotlin.test.Test

class GradleBuildTest : HarnessGradle() {

    @Test
    fun `ktor plugin handles requests`() = assertBuilds("ktor", tasks = listOf(":test"))

    @Test
    fun `expect actual gets resolved`() = assertMatchesGolden("expect_actual", tasks = listOf("assemble", ":jvmRun"))

    @Test
    fun `an expect Include property resolves to its actual`() =
        assertBuilds("expect_actual_property", tasks = listOf("assemble", ":jvmRun"))

    @Test
    fun `an incremental build after editing an unrelated file keeps the entry point factory`() =
        assertRebuildsAfterEdit("incremental_edit", "src/main/Screen.kt", "\"first\"", "\"second\"")

    @Test
    fun `the Gradle plugin sets up a JVM project in one line`() =
        assertBuilds("gradle_plugin_jvm", tasks = listOf(":run"))

    @Test
    fun `the Gradle plugin sets up every target of a multiplatform project`() =
        assertBuilds("gradle_plugin_multiplatform", tasks = listOf(":compileKotlinJs", ":jvmRun"))

    @Test
    fun `the Gradle plugin adds Spike in the version it was published as`() = assertBuilds(
        "gradle_plugin_version",
        tasks = listOf(":assertSpikeVersion"),
        arguments = listOf("-PVERSION_NAME=1.2.3")
    )

    @Test
    fun `a module marked spike library contributes its ViewModels to the app's entry point without an export`() =
        assertBuilds("gradle_plugin_viewmodel_library", tasks = listOf(":app:run"))

    @Test
    fun `a library module's own test source set still gets a full ViewModel entry point`() =
        assertBuilds("gradle_plugin_viewmodel_library", tasks = listOf(":view:runTestMain"))

    @Test
    fun `two app-role modules with the same project name fail as an ambiguous ViewModel entry point`() =
        assertBuildFails(
            "gradle_plugin_viewmodel_same_name",
            listOf(":app:compileKotlin"),
            "SPIKE-VIEWMODEL-ENTRY-POINT-AMBIGUOUS",
            "Modules: :a:ui, :b:ui."
        )

    @Test
    fun `two app-role JS modules with the same project name fail as an ambiguous ViewModel entry point`() =
        assertBuildFails(
            "gradle_plugin_viewmodel_same_name_js",
            listOf(":app:compileKotlinJs"),
            "SPIKE-VIEWMODEL-ENTRY-POINT-AMBIGUOUS",
            "Modules: :a:ui, :b:ui."
        )

    @Test
    fun `modules whose paths differ only in a dash or an underscore fail as an ambiguous ViewModel entry point`() =
        assertBuildFails(
            "gradle_plugin_viewmodel_dash_underscore",
            listOf(":app:compileKotlin"),
            "SPIKE-VIEWMODEL-ENTRY-POINT-AMBIGUOUS",
            "Modules: :a-b, :a_b."
        )

    @Test
    fun `modules whose paths differ only in where an underscore splits them fail as an ambiguous ViewModel entry point`() =
        assertBuildFails(
            "gradle_plugin_viewmodel_nested_underscore",
            listOf(":app:compileKotlin"),
            "SPIKE-VIEWMODEL-ENTRY-POINT-AMBIGUOUS",
            "Modules: :a:b_c, :a_b:c."
        )

    @Test
    fun `ViewModels in a library's main and test fixtures both reach the library's own tests`() =
        assertBuilds("gradle_plugin_viewmodel_test_fixtures_viewmodel", tasks = listOf(":view:runTestMain"))

    @Test
    fun `a project path that KSP cannot carry as an option fails naming the project`() =
        assertBuildFails(
            "gradle_plugin_module_id_equals",
            listOf(":a=b:compileKotlin"),
            "Spike cannot pass the module id :a=b to KSP, which splits processor options at '=' and whitespace. Rename the project."
        )

    @Test
    fun `the Gradle plugin runs Spike in the unit and instrumented tests of an Android library`() = assertBuilds(
        "gradle_plugin_android_library",
        tasks = listOf(":compileDebugUnitTestKotlin", ":compileDebugAndroidTestKotlin")
    )

    @Test
    fun `the Gradle plugin runs Spike in the tests of a multiplatform Android target`() = assertBuilds(
        "gradle_plugin_multiplatform_android_target",
        tasks = listOf(":compileDebugUnitTestKotlinAndroid", ":compileDebugAndroidTestKotlinAndroid")
    )

    @Test
    fun `the Gradle plugin applied before the Kotlin plugin still passes the library role`() =
        assertBuilds("gradle_plugin_spike_before_kotlin", tasks = listOf(":app:run", ":view:runTestMain"))

    @Test
    fun `the Gradle plugin runs Spike in a custom JVM test suite`() =
        assertBuilds("gradle_plugin_integration_test_suite", tasks = listOf(":runIntegrationMain"))

    @Test
    fun `test fixtures of a library module keep the library role`() =
        assertBuilds("gradle_plugin_viewmodel_test_fixtures", tasks = listOf(":app:runTestMain"))

    @Test
    fun `a multiplatform library module contributes ViewModels to the app and its own tests get a full entry point`() =
        assertBuilds("gradle_plugin_viewmodel_multiplatform_library", tasks = listOf(":app:jvmRun", ":view:runTestMain"))

    @Test
    fun `a plugin module that does not set spike library generates the ViewModel entry point`() =
        assertBuilds("gradle_plugin_viewmodel_default_app", tasks = listOf(":runMain"))

    @Test
    fun `a test source set reuses the app's ViewModel entry point and can replace its bindings`() =
        assertBuilds("gradle_plugin_viewmodel_test_source_set", tasks = listOf(":runTestMain"))

    @Test
    fun `exports are taken into account`() = assertMatchesGolden(
        "multi_module",
        template = "multi_module",
        tasks = listOf(":app:run"),
        modules = listOf("app", "library")
    )

    @Test
    fun `an export depending on another module's export with imports builds and runs`() =
        assertBuilds("export_depends_on_export", tasks = listOf(":app:run"))

}
