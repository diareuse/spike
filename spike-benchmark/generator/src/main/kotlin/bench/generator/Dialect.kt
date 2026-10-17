package bench.generator

interface Dialect {
    val id: String
    fun nodesFile(index: Int, nodes: List<Node>): String
    fun graphFile(tree: DependencyTree, nodeFiles: Int): String
    fun screensFile(): String
}

internal fun Node.parameters(): String =
    dependencies.joinToString { "val ${it.replaceFirstChar(Char::lowercase)}: $it" }

internal fun List<Node>.render(imports: String = "", node: (Node) -> String): String =
    imports + joinToString("\n", transform = node)
