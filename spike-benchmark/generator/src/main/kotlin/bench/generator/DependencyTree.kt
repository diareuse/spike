package bench.generator

data class Node(val name: String, val dependencies: List<String>)

data class DependencyTree(val nodes: List<Node>) {

    init {
        require(nodes.isNotEmpty()) { "A dependency tree needs at least its root node" }
    }

    val root: Node get() = nodes.first()

}
