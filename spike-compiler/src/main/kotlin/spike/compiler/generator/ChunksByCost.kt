package spike.compiler.generator

class ChunksByCost(private val budget: Int) {

    fun <T> split(items: List<T>, cost: (T) -> Int): List<List<T>> {
        val chunks = mutableListOf<MutableList<T>>()
        var spent = 0
        for (item in items) {
            val itemCost = cost(item)
            if (chunks.isEmpty() || spent + itemCost > budget) {
                chunks += mutableListOf<T>()
                spent = 0
            }
            chunks.last() += item
            spent += itemCost
        }
        return chunks
    }

}
