package spike.compiler.harness

import java.io.File

enum class Libraries(private val excluded: List<String>) {
    Kotlin(listOf("androidx", "lifecycle", "savedstate", "ktor")),
    Androidx(listOf("ktor"));

    fun files(): List<File> = testClasspath.filterNot { file -> (excluded + tooling).any { it in file.name } }

    private companion object {
        val tooling = listOf("symbol-processing", "compiler-embeddable", "gradle")
        val testClasspath = System.getProperty("java.class.path").split(File.pathSeparator).map(::File)
    }
}
