package spike.compiler.graph

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class ImportNameTest {

    @Test
    fun `types differing only in where the package ends get different names`() = assertNotEquals(
        simple("com.example.a", "BConfig").importName(),
        simple("com.example.aB", "Config").importName()
    )

    @Test
    fun `a nullable type and its non-null type get different names`() =
        assertNotEquals(simple("a", "Foo").importName(), simple("a", "Foo", nullable = true).importName())

    @Test
    fun `an underscore and a package separator get different names`() =
        assertNotEquals(simple("a", "b_c").importName(), simple("a.b", "c").importName())

    @Test
    fun `an escaped name reads back as the original text`() {
        val text = "function com.example.lib.libraryTom: kotlin.collections.Map<kotlin.String?, a.b_c>.1"

        assertEquals(text, text.escapedName().unescapedName())
    }

    @Test
    fun `a plain type keeps a readable name`() =
        assertEquals("kotlin_String", simple("kotlin", "String").importName())

    @Test
    fun `a root-package type never becomes a keyword or a generated member name`() =
        assertEquals("_in", simple("", "in").importName())

    @Test
    fun `names are Kotlin identifiers`() {
        val types = listOf(
            simple("", "Root"),
            simple("a", "Foo", nullable = true),
            Type.Parametrized(
                simple("kotlin.collections", "Map"),
                listOf(simple("kotlin", "Int"), simple("a", "B")),
                false
            ),
            simple("a", "Ünïcode"),
        )
        for (type in types) {
            val name = type.importName()
            assertTrue(name.all { it.isLetterOrDigit() || it == '_' } && !name.first().isDigit(), name)
        }
    }

    private fun simple(packageName: String, name: String, nullable: Boolean = false) =
        Type.Simple(packageName, name, nullable)
}
