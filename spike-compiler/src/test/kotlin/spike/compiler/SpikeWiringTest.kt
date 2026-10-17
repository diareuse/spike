package spike.compiler

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class SpikeWiringTest {

    private val root = File("..").canonicalFile
    private val modules = listOf(
        "spike-compiler",
        "spike-compiler-conversion",
        "spike-compiler-jsr",
        "spike-compiler-dagger",
        "spike-compiler-kotlin-inject",
        "spike-compiler-metro",
        "spike-compiler-koin",
        "spike-compiler-androidx",
        "spike-gradle-plugin",
        "spike-benchmark/generator",
    )

    @Test
    fun `no production code constructs a class that Spike can inject`() {
        val sources = modules.flatMap { module ->
            root.resolve("$module/src/main/kotlin").walk().filter { it.extension == "kt" }.toList()
        }
        val texts = sources.map { it.readText() }
        val declared = texts.flatMap { text -> DeclaredClass.findAll(text).map { it.groupValues[1] }.toList() }
        val unique = declared.groupingBy { it }.eachCount().filterValues { it == 1 }.keys
        val injectable = texts.flatMap { text -> IncludedClass.findAll(text).map { it.groupValues[1] }.toList() }
            .filter { it in unique }
        val constructions = sources.flatMap { file ->
            file.readLines().mapIndexedNotNull { index, line ->
                val constructed = injectable.firstOrNull { name ->
                    Regex("""(?<![\w.:])$name\(""").containsMatchIn(line) &&
                        !Regex("""class\s+$name\b""").containsMatchIn(line)
                }
                constructed?.let { "${file.relativeTo(root)}:${index + 1} constructs $it" }
            }
        }
        assertEquals(emptyList(), constructions)
    }

    private companion object {
        val DeclaredClass = Regex("""\bclass\s+(\w+)""")
        val IncludedClass = Regex("""@Include\s+(?:@[\w.()" ]+\s+)*(?:internal\s+)?class\s+(\w+)""")
    }
}
