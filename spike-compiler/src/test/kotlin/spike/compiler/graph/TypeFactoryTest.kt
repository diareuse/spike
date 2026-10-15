package spike.compiler.graph

import spike.compiler.graph.TypeFactory.Companion.invertDependencyTree
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.Assertions.assertTimeoutPreemptively
import java.time.Duration

class TypeFactoryTest {

    @Test
    fun `a dependency shared by several consumers is listed once`() {
        val shared = leaf("Shared")
        val root = node("Root", node("Left", shared), node("Right", shared))

        assertEquals(1, root.invertDependencyTree().count { it === shared })
    }

    @Test
    fun `every factory is listed after all of its dependencies and the root comes last`() {
        val shared = leaf("Shared")
        val left = node("Left", shared)
        val right = node("Right", shared, left)
        val root = node("Root", left, right)

        val order = root.invertDependencyTree()

        assertEquals(root, order.last())
        for (factory in order) for (dependency in factory.dependencies) {
            assertTrue(order.indexOf(dependency) < order.indexOf(factory), "$dependency must precede ${factory.type}")
        }
    }

    @Test
    fun `a deferred dependency is listed without the factory it defers`() {
        val deferred = leaf("Deferred")
        val provider = TypeFactory.Provides(
            Type.Parametrized(BuiltInTypes.Provider, listOf(deferred.type), false),
            node("Target", deferred),
        )
        val root = node("Root", provider)

        assertEquals(listOf<TypeFactory>(provider, root), root.invertDependencyTree())
    }

    @Test
    fun `a wide lattice of shared dependencies is walked once per node`() {
        var level = listOf(leaf("Left30"), leaf("Right30"))
        for (index in 29 downTo 0) {
            level = listOf(node("Left$index", *level.toTypedArray()), node("Right$index", *level.toTypedArray()))
        }

        assertEquals(61, level.first().invertDependencyTree().size)
    }

    @Test
    fun `two separately built deep chains are equal on a small thread stack`() {
        val first = chain(20_000)
        val second = chain(20_000)

        onSmallStack { assertEquals(first, second) }
    }

    @Test
    fun `two separately built chains differing only at the bottom are not equal`() {
        val first = chain(50)
        val second = chain(50, bottom = "Other")

        onSmallStack { assertNotEquals(first, second) }
    }

    @Test
    fun `factories differing only in being singletons are not equal`() {
        val plain = node("Root", leaf("Leaf"))
        val singleton = plain.copy(singleton = true)

        assertNotEquals<TypeFactory>(plain, singleton)
    }

    @Test
    fun `two separately built wide lattices compare equal without walking every path`() {
        assertTimeoutPreemptively(Duration.ofSeconds(10)) {
            assertEquals(lattice(60), lattice(60))
        }
    }

    @Test
    fun `a deep chain can be printed on a small thread stack`() {
        val deep = chain(20_000)

        onSmallStack { deep.toString() }
    }

    @Test
    fun `two separately built deep chains differing at the bottom are not equal on a small thread stack`() {
        val first = chain(20_000)
        val second = chain(20_000, bottom = "Other")

        onSmallStack { assertNotEquals(first, second) }
    }

    @Test
    fun `maps with the same entries in a different order are equal`() {
        val type = Type.Parametrized(BuiltInTypes.Map, listOf(type("Key"), type("Value")), false)
        val first = TypeFactory.MultibindsMap(type, linkedMapOf("a" to chain(3), "b" to chain(4)))
        val second = TypeFactory.MultibindsMap(type, linkedMapOf("b" to chain(4), "a" to chain(3)))

        assertEquals<TypeFactory>(first, second)
    }

    @Test
    fun `maps binding a key to different values are not equal`() {
        val type = Type.Parametrized(BuiltInTypes.Map, listOf(type("Key"), type("Value")), false)
        val first = TypeFactory.MultibindsMap(type, mapOf("a" to chain(3)))
        val second = TypeFactory.MultibindsMap(type, mapOf("a" to chain(3, bottom = "Other")))

        assertNotEquals<TypeFactory>(first, second)
    }

    @Test
    fun `collections with the same entries in a different order are not equal`() {
        val first = TypeFactory.MultibindsCollection(type("Set"), listOf(leaf("A"), leaf("B")), BuiltInTypes.Set)
        val second = TypeFactory.MultibindsCollection(type("Set"), listOf(leaf("B"), leaf("A")), BuiltInTypes.Set)

        assertNotEquals<TypeFactory>(first, second)
    }

    @Test
    fun `methods differing only in the member they call are not equal`() {
        val first = method("provideCat", leaf("Dog"))
        val second = method("provideOtherCat", leaf("Dog"))

        assertNotEquals<TypeFactory>(first, second)
    }

    @Test
    fun `methods over separately built equal dependencies are equal`() {
        assertEquals<TypeFactory>(method("provideCat", chain(10)), method("provideCat", chain(10)))
    }

    @Test
    fun `providers over separately built equal factories are equal`() {
        val provider = Type.Parametrized(BuiltInTypes.Provider, listOf(type("Link0")), false)

        assertEquals<TypeFactory>(TypeFactory.Provides(provider, chain(10)), TypeFactory.Provides(provider, chain(10)))
    }

    @Test
    fun `a provider and a lazy over the same factory are not equal`() {
        val target = chain(3)
        val provider = TypeFactory.Provides(Type.Parametrized(BuiltInTypes.Provider, listOf(target.type), false), target)
        val lazy = TypeFactory.Memorizes(Type.Parametrized(BuiltInTypes.Lazy, listOf(target.type), false), target)

        assertNotEquals<TypeFactory>(provider, lazy)
    }

    private fun type(name: String) = Type.Simple("test", name, false)

    private fun method(name: String, vararg dependencies: TypeFactory) = TypeFactory.Method(
        type = type("Cat"),
        member = Member.Method("test", name, type("Cat")),
        invocation = Invocation(dependencies.map { Parameter(it.type.simpleName.lowercase(), it.type, false) }, false),
        singleton = false,
        dependencies = dependencies.toList(),
    )

    private fun chain(depth: Int, bottom: String = "Link$depth"): TypeFactory {
        var link: TypeFactory = leaf(bottom)
        for (index in depth - 1 downTo 0) link = node("Link$index", link)
        return link
    }

    private fun lattice(depth: Int): TypeFactory {
        var level = listOf(leaf("Left$depth"), leaf("Right$depth"))
        for (index in depth - 1 downTo 0) {
            level = listOf(node("Left$index", *level.toTypedArray()), node("Right$index", *level.toTypedArray()))
        }
        return level.first()
    }

    private fun leaf(name: String) = node(name)

    private fun node(name: String, vararg dependencies: TypeFactory) = TypeFactory.Class(
        type = Type.Simple("test", name, false),
        invocation = Invocation(dependencies.map { Parameter(it.type.simpleName.lowercase(), it.type, false) }, false),
        singleton = false,
        dependencies = dependencies.toList(),
    )

}
