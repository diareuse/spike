package bench.generator

import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SourceTreeTest {

    private val tree = DependencyTree(
        listOf(Node("Node0", listOf("Node1", "Node2")), Node("Node1", emptyList()), Node("Node2", emptyList()))
    )

    @Test
    fun `splits nodes into files of the given size plus one graph file`() {
        val directory = Files.createTempDirectory("sources").toFile()
        SourceTree(tree, nodesPerFile = 2).writeTo(directory, DialectManual())
        val written = directory.resolve("bench/manual").list()!!.sorted()
        assertEquals(listOf("Graph.kt", "Nodes0.kt", "Nodes1.kt"), written)
    }

    @Test
    fun `every file declares the dialect package`() {
        val directory = Files.createTempDirectory("sources").toFile()
        SourceTree(tree, nodesPerFile = 2).writeTo(directory, DialectKotlinInject())
        val files = directory.resolve("bench/kotlininject").listFiles()!!
        assertTrue(files.all { it.readText().startsWith("package bench.kotlininject\n") })
    }

}
