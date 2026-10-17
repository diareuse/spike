package spike.compiler.generator

import spike.Include
class LinearLayoutByCost(
    private val chunks: ChunksByCost,
    private val cost: (LinearStep) -> Int,
) {

    fun of(program: LinearProgram): LinearLayout {
        val consumers = program.steps.indices.associateWith { mutableListOf<Int>() }
        for ((index, step) in program.steps.withIndex()) {
            for (argument in step.arguments) consumers.getValue(argument) += index
        }
        val parts = chunks.split(program.steps.withIndex().toList()) { (_, step) -> cost(step) }
        val partOf = parts.withIndex()
            .flatMap { (part, steps) -> steps.map { (index, _) -> index to part } }
            .toMap()
        val slots = program.steps.indices
            .filter { index -> consumers.getValue(index).any { partOf.getValue(it) > partOf.getValue(index) } }
            .withIndex()
            .associate { (slot, index) -> index to slot }
        return LinearLayout(parts, slots, consumers, partOf)
    }

    companion object {
        private const val StepCost = 32
        private const val ArgumentCost = 24
        private const val ChunkBudget = 4000

        @Include
        fun default(): LinearLayoutByCost =
            LinearLayoutByCost(ChunksByCost(ChunkBudget)) { step -> StepCost + ArgumentCost * step.arguments.size }
    }
}
