package bench.generator

import spike.BindTarget
import spike.Include

@Include
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

    override fun screensFile() = """
        class Handle(val id: Int)

        class AppState

        class Screen(val handle: Handle, val state: AppState)

        class App {
            val state = AppState()
        }

        fun createApp(): App = App()

        fun openScreen(app: App, id: Int): Screen = Screen(Handle(id), app.state)

    """.trimIndent()

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(10)
        val DialectManual.asDialect: Dialect
            get() = this
    }
}
