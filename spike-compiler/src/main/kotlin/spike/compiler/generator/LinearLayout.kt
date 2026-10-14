package spike.compiler.generator

class LinearLayout(
    val parts: List<List<IndexedValue<LinearStep>>>,
    val slots: Map<Int, Int>,
    private val consumers: Map<Int, List<Int>>,
    private val partOf: Map<Int, Int>,
) {
    val root: Int get() = parts.last().last().index

    fun partOf(step: Int): Int = partOf.getValue(step)

    fun slotOf(step: Int): Int? = slots[step]

    fun isLocal(step: Int): Boolean = consumers.getValue(step).any { partOf(it) == partOf(step) }
}
