package bench.generator

class DialectKotlinInject : Dialect {
    override val id = "kotlin-inject"

    override fun nodesFile(index: Int, nodes: List<Node>) =
        nodes.render("import me.tatarka.inject.annotations.Inject\n\n") { "@Inject\nclass ${it.name}(${it.parameters()})\n" }

    override fun graphFile(tree: DependencyTree, nodeFiles: Int) = """
        @me.tatarka.inject.annotations.Component
        abstract class Graph {
            abstract val root: ${tree.root.name}
        }

        fun createGraph(): Graph = Graph::class.create()

        fun resolve(graph: Graph): ${tree.root.name} = graph.root

        fun resolve(): ${tree.root.name} = resolve(createGraph())

    """.trimIndent()
}
