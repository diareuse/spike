package spike.compiler.processor

import spike.compiler.graph.Constructor
import spike.compiler.graph.GraphStore
import spike.compiler.graph.Invocation
import spike.compiler.graph.Qualifier
import spike.compiler.graph.Type
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProductionBindingsTest {

    private val source = type("Source")
    private val clock = type("Clock")
    private val screen = type("Screen")
    private val missing = type("Missing")
    private val namedSource = Type.Qualified(source, listOf(Qualifier(type("Named"), emptyList())))

    private val bindings = ProductionBindings(
        store = GraphStore(constructors = listOf(Constructor(screen, Invocation(emptyList(), false), false))),
        exported = setOf(source, clock),
    )

    @Test
    fun `binds a type the production store binds`() {
        assertTrue(bindings.binds(screen))
    }

    @Test
    fun `binds a type another module exports`() {
        assertTrue(bindings.binds(source))
    }

    @Test
    fun `does not bind a type neither the store nor an export provides`() {
        assertFalse(bindings.binds(missing))
    }

    @Test
    fun `does not bind a qualified type when only the unqualified type is exported`() {
        assertFalse(bindings.binds(namedSource))
    }

    @Test
    fun `without an exported type keeps the other exports`() {
        val remaining = bindings.without(source)

        assertFalse(remaining.binds(source))
        assertEquals(setOf(clock), remaining.exported)
        assertTrue(remaining.binds(screen))
    }

    @Test
    fun `without a store binding keeps every export`() {
        val remaining = bindings.without(screen)

        assertFalse(remaining.binds(screen))
        assertEquals(setOf(source, clock), remaining.exported)
    }

    private fun type(name: String) = Type.Simple("test", name, false)
}
