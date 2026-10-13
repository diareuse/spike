package bench.generator

class DialectSpike : Dialect {
    override val id = "spike"

    override fun nodesFile(index: Int, nodes: List<Node>) = nodes.render {
        "@spike.Include\nclass ${it.name}(${it.parameters()})\n"
    }

    override fun graphFile(tree: DependencyTree, nodeFiles: Int) = """
        @spike.EntryPoint
        interface Graph {
            val root: ${tree.root.name}

            companion object
        }

        fun createGraph(): Graph = Graph()

        fun resolve(graph: Graph): ${tree.root.name} = graph.root

        fun resolve(): ${tree.root.name} = resolve(createGraph())

    """.trimIndent()
}
