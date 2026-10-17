package spike.compiler.conversion

import com.squareup.kotlinpoet.FunSpec
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderedTest {

    @Test
    fun `entries come in ascending order of their keys`() =
        assertEquals(listOf("a", "b", "c"), mapOf(30 to "c", 10 to "a", 20 to "b").ordered())

    @Test
    fun `the lowest ordered decorator wraps the core first`() {
        val applied = mutableListOf<String>()
        val decorators = mapOf(
            20 to IncludeFunctionDecorator<Subject> { inner -> IncludeFunction { applied += "outer"; inner.of(it) } },
            10 to IncludeFunctionDecorator<Subject> { inner -> IncludeFunction { applied += "inner"; inner.of(it) } },
        )

        decorators.applyTo { applied += "core"; FunSpec.builder("f").build() }.of(Subject())

        assertEquals(listOf("outer", "inner", "core"), applied)
    }

    private class Subject : Declared {
        override val declaration get() = error("unused")
    }
}
