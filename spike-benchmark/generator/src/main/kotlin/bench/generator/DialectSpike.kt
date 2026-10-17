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

    override fun screensFile() = """
        class Handle(val id: Int)

        @spike.Singleton
        @spike.Include
        class AppState

        @spike.Include
        class Screen(val handle: Handle, val state: AppState)

        @OptIn(spike.InternalSpikeApi::class)
        @spike.EntryPoint
        interface ScreenGraph {
            val screen: Screen

            @spike.EntryPoint.Factory
            @spike.EntryPoint.Factory.SharesSingletons
            interface Factory {
                fun create(handle: Handle): ScreenGraph
            }

            companion object
        }

        typealias App = ScreenGraph.Factory

        fun createApp(): App = ScreenGraph.factory()

        fun openScreen(app: App, id: Int): Screen = app.create(Handle(id)).screen

    """.trimIndent()
}
