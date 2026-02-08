package spike.compiler

import spike.compiler.harness.TestHarness
import kotlin.test.Test

class EntryPointTest : TestHarness() {

    @Test
    fun `EntryPoint without companion fails`() = testFail("no_companion")

    @Test
    fun `circular dependency fails`() = testFail("circular")

    @Test
    fun `EntryPoint must not be class`() = testFail("klass")

    @Test
    fun `standalone EntryPoint with properties`() = test("entrypoint")

    @Test
    fun `EntryPoint factory is created`() = test("entry_factory")

    @Test
    fun `complex tree is resolved`() = test("complex")

    @Test
    fun `binds interfaces to concrete classes`() = test("bind")

    @Test
    fun `EntryPoint generates providers`() = test("provider")

    @Test
    fun `entries with qualifiers resolve`() = test("qualifier")

    @Test
    fun `multiple constructors must be marked`() = testFail("no_marked_constructors")

    @Test
    fun `multiple constructors are marked`() = test("marked_constructors")

    @Test
    fun `multi binds are possible`() = test("multibinding")

    @Test
    fun `objects are composed`() = test("compositor")

    @Test
    fun `out variance is preserved`() = test("out_variance")

    @Test
    fun `androidx viewmodels are found`() = test("androidx_viewmodel")

}