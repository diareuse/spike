package bench.generator

import spike.Include
import java.io.File

@Include
class ProfilerScenarios(private val sources: SourceTree) {

    fun writeTo(file: File, dialects: List<Dialect>) {
        file.writeText(dialects.joinToString("\n") { scenariosOf(it) })
    }

    private fun scenariosOf(dialect: Dialect): String {
        val key = dialect.id.filter(Char::isLetterOrDigit)
        val task = "\":graphs:${dialect.id}:classes\""
        val leaf = "graphs/${dialect.id}/src/main/kotlin/${sources.packageOf(dialect).replace('.', '/')}/" +
            sources.lastNodesFile
        return """
            clean_$key {
                title = "${dialect.id} clean build"
                tasks = [$task]
                cleanup-tasks = [":graphs:${dialect.id}:clean"]
            }
            abi_$key {
                title = "${dialect.id} ABI change"
                tasks = [$task]
                apply-abi-change-to = "$leaf"
            }
            nonabi_$key {
                title = "${dialect.id} non-ABI change"
                tasks = [$task]
                apply-non-abi-change-to = "$leaf"
            }

        """.trimIndent()
    }

}
