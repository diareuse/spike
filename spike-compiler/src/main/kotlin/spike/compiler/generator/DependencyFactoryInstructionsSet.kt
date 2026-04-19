package spike.compiler.generator

class DependencyFactoryInstructionsSet {
    val instructions = mutableListOf<Int>()
    private var start = -1
    fun start() = instructions.size.also {
        start = it
    }

    fun end(contextSize: Int): Int {
        instructions.add(start, contextSize)
        return (instructions.size - start).also {
            start = -1
        }
    }

    fun add(instruction: Int) {
        instructions.add(instruction)
    }
}
