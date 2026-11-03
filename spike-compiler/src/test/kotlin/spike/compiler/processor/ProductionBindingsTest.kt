package spike.compiler.processor

import spike.compiler.graph.Constructor
import spike.compiler.graph.ExportedUse
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
    private val repository = type("Repository")
    private val exports = type("DataExports")
    private val repositoryUsingSource = CompiledUse(
        ExportedUse("property repository", repository.toString(), setOf(source.toString())),
        exports,
    )

    private val welcomeUsingRepository = CompiledUse(
        ExportedUse("property welcome", type("Welcome").toString(), setOf(repository.toString())),
        type("WelcomeExports"),
    )

    private val bindings = ProductionBindings(
        store = GraphStore(constructors = listOf(Constructor(screen, Invocation(emptyList(), false), false))),
        exported = setOf(source, clock),
        uses = listOf(welcomeUsingRepository, repositoryUsingSource),
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

    @Test
    fun `lists the exported bindings built with a type and every export downstream of them`() {
        assertEquals(listOf(repositoryUsingSource, welcomeUsingRepository), bindings.usersOf(source))
    }

    @Test
    fun `lists only the exported bindings downstream of a type that is used further down the chain`() {
        assertEquals(listOf(welcomeUsingRepository), bindings.usersOf(repository))
    }

    @Test
    fun `lists no exported binding for a type no export is built with`() {
        assertEquals(emptyList(), bindings.usersOf(clock))
    }

    @Test
    fun `without an exported binding's own type it no longer counts as a user`() {
        assertEquals(emptyList(), bindings.without(repository).usersOf(source))
        assertEquals(listOf(welcomeUsingRepository), bindings.without(repository).usersOf(repository))
    }

    private fun type(name: String) = Type.Simple("test", name, false)
}
