package bench.generator

class DialectKoin : Dialect {
    override val id = "koin"

    override fun nodesFile(index: Int, nodes: List<Node>): String {
        val classes = nodes.render("import org.koin.dsl.module\n\n") { "class ${it.name}(${it.parameters()})\n" }
        val factories = nodes.joinToString("\n") { node ->
            "    factory { ${node.name}(${node.dependencies.joinToString { "get()" }}) }"
        }
        return "$classes\nval nodes$index = module {\n$factories\n}\n"
    }

    override fun graphFile(tree: DependencyTree, nodeFiles: Int): String {
        val modules = List(nodeFiles) { "nodes$it" }.joinToString()
        return """
            typealias Graph = org.koin.core.Koin

            fun createGraph(): Graph = org.koin.dsl.koinApplication {
                modules($modules)
            }.koin

            fun resolve(graph: Graph): ${tree.root.name} = graph.get()

            fun resolve(): ${tree.root.name} = resolve(createGraph())

        """.trimIndent()
    }

    override fun screensFile() = """
        class Handle(val id: Int)

        class AppState

        class Screen(val handle: Handle, val state: AppState)

        typealias App = org.koin.core.Koin

        val screens = org.koin.dsl.module {
            single { AppState() }
            factory { (handle: Handle) -> Screen(handle, get()) }
        }

        fun createApp(): App = org.koin.dsl.koinApplication { modules(screens) }.koin

        fun openScreen(app: App, id: Int): Screen = app.get { org.koin.core.parameter.parametersOf(Handle(id)) }

    """.trimIndent()
}
