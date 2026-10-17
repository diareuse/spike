package bench.generator

import spike.BindTarget
import spike.Include

@Include
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

    override fun screensFile() = """
        class Handle(val id: Int)

        @javax.inject.Singleton
        class AppState @javax.inject.Inject constructor()

        class Screen @javax.inject.Inject constructor(val handle: Handle, val state: AppState)

        @dagger.Subcomponent
        interface ScreenComponent {
            val screen: Screen

            @dagger.Subcomponent.Factory
            interface Factory {
                fun create(@dagger.BindsInstance handle: Handle): ScreenComponent
            }
        }

        @javax.inject.Singleton
        @dagger.Component
        interface App {
            val screens: ScreenComponent.Factory
        }

        fun createApp(): App = DaggerApp.create()

        fun openScreen(app: App, id: Int): Screen = app.screens.create(Handle(id)).screen

    """.trimIndent()

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(30)
        val DialectDagger.asDialect: Dialect
            get() = this
    }
}
