package spike.compiler.generator

class LinearGuards(
    private val guards: Map<Int, Int>,
    val table: String,
) {
    fun guardOf(step: Int): Int? = guards[step]

    companion object {
        val None = LinearGuards(emptyMap(), "")
    }
}
