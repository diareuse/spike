package spike.compiler.harness

class TestFixtureGenerator {

    fun generate(maxDepth: Int) {
        val root = generateNode(0, maxDepth)
        println(root.asSequence().joinToString("\n"))
    }

    private fun generateNode(depth: Int, maxDepth: Int): Node {
        return when (depth == 0) {
            true -> Node(List((1..3).random()) { generateNode(1, maxDepth) }, 0, "Root")
            else -> Node(
                leaves = if (depth == maxDepth) emptyList()
                else List((1..3).random()) { generateNode(depth + 1, maxDepth) },
                depth = depth
            )
        }
    }

    class Node(
        val leaves: List<Node>,
        val depth: Int,
        val name: String = generateName(depth),
    ) {

        fun asSequence(): Sequence<Node> = sequence {
            yield(this@Node)
            for (leaf in leaves) {
                yieldAll(leaf.asSequence())
            }
        }

        override fun toString(): String {
            val params = leaves.joinToString {
                "val ${it.name.lowercase()}: ${it.name}"
            }
            return """@spike.Include
            |class ${name}($params)
            """.trimMargin("|")
        }

        companion object {
            fun generateName(depth: Int): String {
                val chars = buildList {
                    addAll('0'..'9')
                    addAll('a'..'z')
                    addAll('A'..'Z')
                }
                return "Node_" + List(10) { chars.random() }.joinToString("") + "_d${depth}"
            }
        }
    }

}
