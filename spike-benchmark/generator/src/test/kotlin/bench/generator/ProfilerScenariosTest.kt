package bench.generator

import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertTrue

class ProfilerScenariosTest {

    private val tree = TreeShapeSeeded(size = 120, maxDependencies = 3, seed = 1).grow()
    private val sources = SourceTree(tree, nodesPerFile = 50)

    @Test
    fun `changes target the last nodes file the source tree writes`() {
        val root = Files.createTempDirectory("bench").toFile()
        sources.writeTo(root.resolve("graphs/kotlin-inject/src/main/kotlin"), DialectKotlinInject())
        val file = root.resolve("scenarios.conf")
        ProfilerScenarios(sources).writeTo(file, listOf(DialectKotlinInject()))
        val leaf = "graphs/kotlin-inject/src/main/kotlin/bench/kotlininject/Nodes2.kt"
        assertContains(file.readText(), "apply-abi-change-to = \"$leaf\"")
        assertTrue(root.resolve(leaf).isFile)
    }

    @Test
    fun `each framework gets clean, ABI and non-ABI scenarios on its own module`() {
        val file = Files.createTempFile("scenarios", ".conf").toFile()
        ProfilerScenarios(sources).writeTo(file, listOf(DialectSpike(), DialectDagger()))
        val text = file.readText()
        for (key in listOf("clean_spike", "abi_spike", "nonabi_spike", "clean_dagger", "abi_dagger", "nonabi_dagger")) {
            assertContains(text, "$key {")
        }
        assertContains(text, "tasks = [\":graphs:dagger:classes\"]")
    }

}
