package spike.compiler.generator

class LinearGuardsOf(
    private val limit: Int,
    private val singletonId: (LinearStep) -> Int?,
) {

    fun of(program: LinearProgram): LinearGuards {
        val steps = program.steps
        val consumers = steps.indices.associateWith { mutableListOf<Int>() }
        for ((index, step) in steps.withIndex()) {
            for (argument in step.arguments) consumers.getValue(argument) += index
        }
        val alwaysRuns = BooleanArray(steps.size)
        val guards = LinkedHashMap<Int, Int>()
        val entries = mutableListOf<List<Int>>()
        for (index in steps.indices.reversed()) {
            val stepConsumers = consumers.getValue(index)
            alwaysRuns[index] = index == steps.lastIndex ||
                stepConsumers.any { alwaysRuns[it] && singletonId(steps[it]) == null }
            if (alwaysRuns[index]) continue
            guards[index] = entries.size
            entries.add(buildList {
                add(stepConsumers.size)
                for (consumer in stepConsumers) {
                    add(guards[consumer] ?: -1)
                    add(singletonId(steps[consumer]) ?: -1)
                }
            })
        }
        val values = listOf(entries.size) + entries.flatten()
        if (guards.isEmpty() || values.size > limit || values.any { it + 1 >= Char.MIN_SURROGATE.code }) {
            return LinearGuards.None
        }
        return LinearGuards(guards, values.joinToString("") { (it + 1).toChar().toString() })
    }

}
