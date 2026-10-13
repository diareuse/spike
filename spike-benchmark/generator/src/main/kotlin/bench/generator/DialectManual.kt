package bench.generator

class DialectManual : Dialect {
    override val id = "manual"

    override fun nodesFile(index: Int, nodes: List<Node>) = nodes.render { node ->
        val arguments = node.dependencies.joinToString { "${factoryOf(it)}()" }
        "class ${node.name}(${node.parameters()})\n" +
            "fun ${factoryOf(node.name)}(): ${node.name} = ${node.name}($arguments)\n"
    }

    override fun graphFile(tree: DependencyTree, nodeFiles: Int) = """
        class Graph {
            val root: ${tree.root.name} get() = ${factoryOf(tree.root.name)}()
        }

        fun createGraph(): Graph = Graph()

        fun resolve(graph: Graph): ${tree.root.name} = graph.root

        fun resolve(): ${tree.root.name} = resolve(createGraph())

    """.trimIndent()

    private fun factoryOf(name: String) = "new$name"
}
