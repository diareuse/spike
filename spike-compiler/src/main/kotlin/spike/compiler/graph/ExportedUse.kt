package spike.compiler.graph

data class ExportedUse(val binding: String, val type: String, val uses: Set<String>) {
    fun encoded() = (listOf(binding, type) + uses.sorted()).joinToString(SEPARATOR)

    companion object {
        private const val SEPARATOR = "\n"

        fun decoded(entry: String): ExportedUse {
            val lines = entry.split(SEPARATOR)
            return ExportedUse(lines[0], lines[1], lines.drop(2).toSet())
        }
    }
}
