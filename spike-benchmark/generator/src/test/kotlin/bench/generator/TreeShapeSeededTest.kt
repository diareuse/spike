package bench.generator

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class TreeShapeSeededTest {

    @Test
    fun `grows exactly the requested number of nodes`() {
        assertEquals(500, TreeShapeSeeded(size = 500, maxDependencies = 3, seed = 1).grow().nodes.size)
    }

    @Test
    fun `same seed grows the same tree`() {
        assertEquals(
            TreeShapeSeeded(size = 300, maxDependencies = 3, seed = 7).grow(),
            TreeShapeSeeded(size = 300, maxDependencies = 3, seed = 7).grow()
        )
    }

    @Test
    fun `different seeds grow different trees`() {
        assertNotEquals(
            TreeShapeSeeded(size = 300, maxDependencies = 3, seed = 7).grow(),
            TreeShapeSeeded(size = 300, maxDependencies = 3, seed = 8).grow()
        )
    }

    @Test
    fun `every node except the root has exactly one parent`() {
        val tree = TreeShapeSeeded(size = 400, maxDependencies = 3, seed = 2).grow()
        val parents = tree.nodes.flatMap { it.dependencies }.groupingBy { it }.eachCount()
        assertEquals(tree.nodes.drop(1).map { it.name }.toSet(), parents.keys)
        assertTrue(parents.values.all { it == 1 })
        assertTrue(tree.root.name !in parents)
    }

    @Test
    fun `no node has more than the maximum number of dependencies`() {
        val tree = TreeShapeSeeded(size = 400, maxDependencies = 2, seed = 3).grow()
        assertTrue(tree.nodes.all { it.dependencies.size <= 2 })
    }

}
