package bench.generator

import java.io.File

class SourceTree(
    private val tree: DependencyTree,
    private val nodesPerFile: Int
) {

    val lastNodesFile: String get() = "Nodes${files.lastIndex}.kt"

    private val files = tree.nodes.chunked(nodesPerFile)

    fun packageOf(dialect: Dialect) = "bench." + dialect.id.filter(Char::isLetterOrDigit)

    fun writeTo(directory: File, dialect: Dialect) {
        val packageName = packageOf(dialect)
        val sources = directory.resolve(packageName.replace('.', '/')).apply {
            deleteRecursively()
            mkdirs()
        }
        files.forEachIndexed { index, nodes ->
            sources.resolve("Nodes$index.kt").writeText(withPackage(packageName, dialect.nodesFile(index, nodes)))
        }
        sources.resolve("Graph.kt").writeText(withPackage(packageName, dialect.graphFile(tree, files.size)))
        sources.resolve("Screens.kt").writeText(withPackage(packageName, dialect.screensFile()))
    }

    private fun withPackage(packageName: String, content: String) = "package $packageName\n\n$content"

}
