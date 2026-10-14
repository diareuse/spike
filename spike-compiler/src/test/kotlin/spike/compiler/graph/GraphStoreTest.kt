package spike.compiler.graph

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GraphStoreTest {

    private val cat = type("Cat")
    private val dog = type("Dog")
    private val pet = type("Pet")

    private val catConstructor = Constructor(cat, Invocation(emptyList(), false), singleton = false)
    private val otherCatConstructor = Constructor(cat, Invocation(emptyList(), false), singleton = true)
    private val petFactory = Factory(pet, Member.Method("test", "pet", pet), Invocation(emptyList(), false), false)
    private val catAsPet = Binder(source = cat, type = pet)

    private val store = GraphStore(
        constructors = listOf(catConstructor, otherCatConstructor),
        factories = listOf(petFactory),
        binders = listOf(catAsPet),
    )

    @Test
    fun `finds the first constructor declared for a type`() {
        assertEquals(catConstructor, store.constructorOf(cat))
    }

    @Test
    fun `finds the factory and the binder declared for a type`() {
        assertEquals(petFactory, store.factoryOf(pet))
        assertEquals(catAsPet, store.binderOf(pet))
    }

    @Test
    fun `finds nothing for a type without bindings`() {
        assertNull(store.constructorOf(dog))
        assertNull(store.factoryOf(dog))
        assertNull(store.binderOf(dog))
        assertEquals(emptyList(), store.bindingsOf(dog))
    }

    @Test
    fun `lists every distinct binding of a type, constructors first, then factories, then binders`() {
        assertEquals(listOf(petFactory, catAsPet), store.bindingsOf(pet))
        assertEquals(listOf(catConstructor, otherCatConstructor), store.bindingsOf(cat))
    }

    @Test
    fun `an overriding definition replaces every binding of its type`() {
        val overridden = store.overriddenBy(otherCatConstructor.asGraphStore())

        assertEquals(listOf(otherCatConstructor), overridden.bindingsOf(cat))
        assertEquals(otherCatConstructor, overridden.constructorOf(cat))
    }

    private fun type(name: String) = Type.Simple("test", name, false)

}
