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

    override fun screensFile() = """
        class Handle(val id: Int)

        @dev.zacsweers.metro.SingleIn(dev.zacsweers.metro.AppScope::class)
        @dev.zacsweers.metro.Inject
        class AppState

        @dev.zacsweers.metro.Inject
        class Screen(val handle: Handle, val state: AppState)

        @dev.zacsweers.metro.GraphExtension
        interface ScreenGraph {
            val screen: Screen

            @dev.zacsweers.metro.GraphExtension.Factory
            interface Factory {
                fun create(@dev.zacsweers.metro.Provides handle: Handle): ScreenGraph
            }
        }

        @dev.zacsweers.metro.DependencyGraph(dev.zacsweers.metro.AppScope::class)
        interface App {
            val screens: ScreenGraph.Factory
        }

        fun createApp(): App = dev.zacsweers.metro.createGraph<App>()

        fun openScreen(app: App, id: Int): Screen = app.screens.create(Handle(id)).screen

    """.trimIndent()
}
