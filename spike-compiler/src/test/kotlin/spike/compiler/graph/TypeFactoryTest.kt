package spike.compiler.graph

import spike.compiler.graph.TypeFactory.Companion.invertDependencyTree
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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

    private fun leaf(name: String) = node(name)

    private fun node(name: String, vararg dependencies: TypeFactory) = TypeFactory.Class(
        type = Type.Simple("test", name, false),
        invocation = Invocation(emptyList(), false),
        singleton = false,
        dependencies = dependencies.toList(),
    )

}
