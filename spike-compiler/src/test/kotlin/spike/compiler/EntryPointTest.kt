package spike.compiler

import spike.compiler.assertion.assertContentEquals
import spike.compiler.harness.BuildResultTasks.compileKotlin
import spike.compiler.harness.BuildResultTasks.kspKotlin
import spike.compiler.harness.TestHarness
import kotlin.test.Test

class EntryPointTest : TestHarness() {

    @Test
    fun `EntryPoint without companion fails`() = runTest(
        label = "no_companion",
        prepare = { useClassPath { it.whitelistModules(Kotlin) }.build() },
        test = { buildAndFail("kspKotlin") },
        verify = { assertFailure(it.kspKotlin) }
    )

    @Test
    fun `circular dependency fails`() = runTest(
        label = "circular",
        prepare = { useClassPath { it.whitelistModules(Kotlin) }.build() },
        test = { buildAndFail("kspKotlin") },
        verify = { assertFailure(it.kspKotlin) }
    )

    @Test
    fun `EntryPoint must not be class`() = runTest(
        label = "klass",
        prepare = { useClassPath { it.whitelistModules(Kotlin) }.build() },
        test = { buildAndFail("kspKotlin") },
        verify = { assertFailure(it.kspKotlin) }
    )

    @Test
    fun `standalone EntryPoint with properties`() = runTest(
        label = "entrypoint",
        prepare = { useClassPath { it.whitelistModules(Kotlin) }.build() },
        test = { build("kspKotlin", "compileKotlin") },
        verify = {
            assertSuccess(it.compileKotlin)
            assertSuccess(it.kspKotlin)
            fixtures.assertContentEquals(outputFiles)
        }
    )

    @Test
    fun `EntryPoint factory is created`() = runTest(
        label = "entry_factory",
        prepare = { useClassPath { it.whitelistModules(Kotlin) }.build() },
        test = { build("kspKotlin", "compileKotlin") },
        verify = {
            assertSuccess(it.compileKotlin)
            assertSuccess(it.kspKotlin)
            fixtures.assertContentEquals(outputFiles)
        }
    )

    @Test
    fun `complex tree is resolved`() = runTest(
        label = "complex",
        prepare = { useClassPath { it.whitelistModules(Kotlin) }.build() },
        test = { build("kspKotlin", "compileKotlin") },
        verify = {
            assertSuccess(it.compileKotlin)
            assertSuccess(it.kspKotlin)
            fixtures.assertContentEquals(outputFiles)
        }
    )

    @Test
    fun `binds interfaces to concrete classes`() = runTest(
        label = "bind",
        prepare = { useClassPath { it.whitelistModules(Kotlin) }.build() },
        test = { build("kspKotlin", "compileKotlin") },
        verify = {
            assertSuccess(it.compileKotlin)
            assertSuccess(it.kspKotlin)
            fixtures.assertContentEquals(outputFiles)
        }
    )

    @Test
    fun `EntryPoint generates providers`() = runTest(
        label = "provider",
        prepare = { useClassPath { it.whitelistModules(Kotlin) }.build() },
        test = { build("kspKotlin", "compileKotlin") },
        verify = {
            assertSuccess(it.compileKotlin)
            assertSuccess(it.kspKotlin)
            fixtures.assertContentEquals(outputFiles)
        }
    )

    @Test
    fun `entries with qualifiers resolve`() = runTest(
        label = "qualifier",
        prepare = { useClassPath { it.whitelistModules(Kotlin) }.build() },
        test = { build("kspKotlin", "compileKotlin") },
        verify = {
            assertSuccess(it.compileKotlin)
            assertSuccess(it.kspKotlin)
            fixtures.assertContentEquals(outputFiles)
        }
    )

    @Test
    fun `multiple constructors must be marked`() = runTest(
        label = "no_marked_constructors",
        prepare = { useClassPath { it.whitelistModules(Kotlin) }.build() },
        test = { buildAndFail("kspKotlin") },
        verify = { assertFailure(it.kspKotlin) }
    )

    @Test
    fun `multiple constructors are marked`() = runTest(
        label = "marked_constructors",
        prepare = { useClassPath { it.whitelistModules(Kotlin) }.build() },
        test = { build("kspKotlin", "compileKotlin") },
        verify = {
            assertSuccess(it.compileKotlin)
            assertSuccess(it.kspKotlin)
            fixtures.assertContentEquals(outputFiles)
        }
    )

    @Test
    fun `multi binds are possible`() = runTest(
        label = "multibinding",
        prepare = { useClassPath { it.whitelistModules(Kotlin) }.build() },
        test = { build("kspKotlin", "compileKotlin") },
        verify = {
            assertSuccess(it.compileKotlin)
            assertSuccess(it.kspKotlin)
            fixtures.assertContentEquals(outputFiles)
        }
    )

    @Test
    fun `objects are composed`() = runTest(
        label = "compositor",
        prepare = { useClassPath { it.whitelistModules(Kotlin) }.build() },
        test = { build("kspKotlin", "compileKotlin") },
        verify = {
            assertSuccess(it.compileKotlin)
            assertSuccess(it.kspKotlin)
            fixtures.assertContentEquals(outputFiles)
        }
    )

    @Test
    fun `out variance is preserved`() = runTest(
        label = "out_variance",
        prepare = { useClassPath { it.whitelistModules(Kotlin) }.build() },
        test = { build("kspKotlin", "compileKotlin") },
        verify = {
            assertSuccess(it.compileKotlin)
            assertSuccess(it.kspKotlin)
            fixtures.assertContentEquals(outputFiles)
        }
    )

    @Test
    fun `androidx viewmodels are found`() = runTest(
        label = "androidx_viewmodel",
        prepare = { useClassPath { it.whitelistModules(Kotlin + Androidx) }.build() },
        test = { build("kspKotlin", "compileKotlin") },
        verify = {
            assertSuccess(it.compileKotlin)
            assertSuccess(it.kspKotlin)
            fixtures.assertContentEquals(outputFiles)
        }
    )

}