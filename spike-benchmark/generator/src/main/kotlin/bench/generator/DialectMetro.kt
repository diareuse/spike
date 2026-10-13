package bench.generator

class DialectMetro : Dialect {
    override val id = "metro"

    override fun nodesFile(index: Int, nodes: List<Node>) =
        nodes.render("import dev.zacsweers.metro.Inject\n\n") { "@Inject\nclass ${it.name}(${it.parameters()})\n" }

    override fun graphFile(tree: DependencyTree, nodeFiles: Int) = """
        @dev.zacsweers.metro.DependencyGraph
        interface Graph {
            val root: ${tree.root.name}
        }

        fun createGraph(): Graph = dev.zacsweers.metro.createGraph<Graph>()

        fun resolve(graph: Graph): ${tree.root.name} = graph.root

        fun resolve(): ${tree.root.name} = resolve(createGraph())

    """.trimIndent()
}
