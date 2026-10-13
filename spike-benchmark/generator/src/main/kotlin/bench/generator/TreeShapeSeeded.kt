package bench.generator

import kotlin.random.Random

class TreeShapeSeeded(
    private val size: Int,
    private val maxDependencies: Int,
    private val seed: Int
) : TreeShape {

    init {
        require(size > 0) { "size must be positive, was $size" }
        require(maxDependencies > 0) { "maxDependencies must be positive, was $maxDependencies" }
    }

    override fun grow(): DependencyTree {
        val random = Random(seed)
        val children = List(size) { mutableListOf<Int>() }
        var next = 1
        var parent = 0
        while (next < size) {
            repeat(random.nextInt(1, maxDependencies + 1).coerceAtMost(size - next)) {
                children[parent] += next++
            }
            parent++
        }
        return DependencyTree(List(size) { index -> Node(nameOf(index), children[index].map(::nameOf)) })
    }

    private fun nameOf(index: Int) = "Node$index"

}
