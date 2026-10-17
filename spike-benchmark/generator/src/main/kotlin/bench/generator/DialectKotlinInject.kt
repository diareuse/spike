package bench.generator

import spike.BindTarget
import spike.Include

@Include
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

    override fun screensFile() = """
        @me.tatarka.inject.annotations.Scope
        annotation class AppScope

        class Handle(val id: Int)

        @AppScope
        @me.tatarka.inject.annotations.Inject
        class AppState

        @me.tatarka.inject.annotations.Inject
        class Screen(val handle: Handle, val state: AppState)

        @AppScope
        @me.tatarka.inject.annotations.Component
        abstract class App {
            abstract val state: AppState
        }

        @me.tatarka.inject.annotations.Component
        abstract class ScreenComponent(
            @me.tatarka.inject.annotations.Component val parent: App,
            @get:me.tatarka.inject.annotations.Provides val handle: Handle,
        ) {
            abstract val screen: Screen
        }

        fun createApp(): App = App::class.create()

        fun openScreen(app: App, id: Int): Screen = ScreenComponent::class.create(app, Handle(id)).screen

    """.trimIndent()

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(50)
        val DialectKotlinInject.asDialect: Dialect
            get() = this
    }
}
