package spike.compiler.harness

import org.gradle.testkit.runner.TaskOutcome
import spike.compiler.assertion.assertContentEquals
import kotlin.test.assertContains
import kotlin.test.assertEquals

abstract class TestHarness {

    protected fun assertMatchesGolden(
        name: String,
        tasks: List<String> = listOf(":run"),
        modules: List<String> = listOf("")
    ) {
        val project = FixtureProject(name)
        project.build(tasks)
        for (module in modules) {
            assertContentEquals(project.golden.resolve(module), project.generatedSources(module))
        }
    }

    protected fun assertBuilds(name: String, tasks: List<String> = listOf(":run")) {
        FixtureProject(name).build(tasks)
    }

    protected fun assertBuildFails(name: String, vararg messages: String, task: String = ":kspKotlin") {
        val result = FixtureProject(name).buildAndFail(listOf(task))
        assertEquals(TaskOutcome.FAILED, result.task(task)?.outcome, result.output)
        for (message in messages) {
            assertContains(result.output, message)
        }
    }

}
