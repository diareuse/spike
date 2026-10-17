package spike.compiler.graph

import kotlin.test.Test
import kotlin.test.assertEquals

class TypeElementTest {

    private val plugin = Type.Simple("test", "Plugin", false)
    private val named = Qualifier(Type.Simple("test", "Named", false), emptyList())

    @Test
    fun `a contribution of the collection type adds its element`() =
        assertEquals(plugin, Type.Parametrized(BuiltInTypes.Set, listOf(plugin), false).elementOf(BuiltInTypes.Set))

    @Test
    fun `a contribution of another collection type adds itself`() {
        val list = Type.Parametrized(BuiltInTypes.List, listOf(plugin), false)

        assertEquals(list, list.elementOf(BuiltInTypes.Set))
    }

    @Test
    fun `a qualified collection contribution adds its element with the same qualifiers`() {
        val qualified = Type.Qualified(Type.Parametrized(BuiltInTypes.Set, listOf(plugin), false), listOf(named))

        assertEquals(Type.Qualified(plugin, listOf(named)), qualified.elementOf(BuiltInTypes.Set))
    }

    @Test
    fun `a nullable qualified type loses its nullability but keeps its qualifiers`() {
        val qualified = Type.Qualified(plugin.copy(nullable = true), listOf(named))

        assertEquals(Type.Qualified(plugin, listOf(named)), qualified.nonNullable())
    }
}
