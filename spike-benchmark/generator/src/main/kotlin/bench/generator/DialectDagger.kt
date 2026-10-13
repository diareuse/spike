package bench.generator

class DialectDagger : Dialect {
    override val id = "dagger"

    override fun nodesFile(index: Int, nodes: List<Node>) = nodes.render("import javax.inject.Inject\n\n") {
        "class ${it.name} @Inject constructor(${it.parameters()})\n"
    }

    override fun graphFile(tree: DependencyTree, nodeFiles: Int) = """
        @dagger.Component
        interface Graph {
            val root: ${tree.root.name}
        }

        fun createGraph(): Graph = DaggerGraph.create()

        fun resolve(graph: Graph): ${tree.root.name} = graph.root

        fun resolve(): ${tree.root.name} = resolve(createGraph())

    """.trimIndent()
}
